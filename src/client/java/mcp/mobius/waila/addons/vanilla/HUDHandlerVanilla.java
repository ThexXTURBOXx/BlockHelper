package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.ConstantRandom;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Block;
import net.minecraft.src.BlockRedstoneOre;
import net.minecraft.src.BlockStep;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.TileEntityMobSpawner;

import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.ItemRecord_recordName;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.bed;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.crops;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.jukebox;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.leave;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.lever;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.log;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.mobSpawner;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.noteBlock;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.redstone;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.repeaterActv;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.repeaterIdle;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.sugarCane;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.web;
import static mcp.mobius.waila.api.SpecialChars.FLAT;
import static mcp.mobius.waila.api.SpecialChars.SHARP;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerVanilla implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerVanilla();

    private static final String[] NOTES = {
            "F" + SHARP + "/G" + FLAT,
            "G",
            "G" + SHARP + "/A" + FLAT,
            "A",
            "A" + SHARP + "/B" + FLAT,
            "B",
            "C",
            "C" + SHARP + "/D" + FLAT,
            "D",
            "D" + SHARP + "/E" + FLAT,
            "E",
            "F"};

    private HUDHandlerVanilla() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        int meta = accessor.getMetadata();

        if (block == redstone)
            return new ItemStack(Item.redstone);

        if (block instanceof BlockRedstoneOre)
            return new ItemStack(Block.oreRedstone);

        if (block == repeaterIdle || block == repeaterActv)
            return new ItemStack(Item.redstoneRepeater);

        if (block == sugarCane)
            return new ItemStack(Item.reed);

        if (block == crops)
            return new ItemStack(Item.wheat);

        if (block == bed)
            return new ItemStack(Item.bed);

        if (block == leave && (meta > 3))
            return new ItemStack(block, 1, meta - 4);

        if (block == log)
            return new ItemStack(block, 1, meta % 4);

        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();

        /* Mob spawner handler */
        if (block == mobSpawner && accessor.getTileEntity() instanceof TileEntityMobSpawner
            && config.get("vanilla.spawntype")) {
            String name = currenttip.get(0);
            String mobname = ((TileEntityMobSpawner) accessor.getTileEntity()).getMobID();
            currenttip.set(0, name + " (" + mobname + ")");
        }

        if (block instanceof BlockStep)
            currenttip.set(0, WHITE + DisplayUtil.itemDisplayNameShort(
                    new ItemStack(block.idDropped(accessor.getMetadata(), ConstantRandom.INSTANCE), 1,
                            accessor.getMetadata() > 3 ? 0 : accessor.getMetadata())));

        if (block == web)
            currenttip.set(0, WHITE + I18n.translate("tile.web.name"));
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        int meta = accessor.getMetadata();

        if (config.get("vanilla.leverstate"))
            if (block == lever) {
                String redstoneOn = (meta & 8) == 0 ? I18n.translate("hud.msg.off") :
                        I18n.translate("hud.msg.on");
                currenttip.add(I18n.translate("hud.msg.state") + ": " + redstoneOn);
            }

        if (config.get("vanilla.repeater"))
            if ((block == repeaterIdle) || (block == repeaterActv)) {
                int tick = ((meta & 0xc) >> 2) + 1;
                if (tick == 1)
                    currenttip.add(I18n.translate("hud.msg.delay") + ": 1 tick");
                else
                    currenttip.add(I18n.translate("hud.msg.delay") + ": " + tick + " ticks");
            }
        if (config.get("vanilla.redstone"))
            if (block == redstone) {
                currenttip.add(I18n.translate("hud.msg.power") + ": " + meta);
            }

        if (config.get("vanilla.jukebox"))
            if (block == jukebox) {
                NBTTagCompound tag = accessor.getNBTData();
                Item record = null;

                if (meta != 0)
                    record = Item.itemsList[(Item.record13.shiftedIndex + meta) - 1];

                try {
                    currenttip.add(record == null
                            ? I18n.translate("hud.msg.empty")
                            : (I18n.translate("hud.msg.record") + ": " +
                               "C418 - " + ItemRecord_recordName.get(record)));
                } catch (Throwable t) {
                    WailaExceptionHandler.handleErr(t, block.getClass().getName() + ":" + meta, currenttip);
                }
            }

        if (config.get("vanilla.noteblock"))
            if (block == noteBlock) {
                int note = accessor.getNBTInteger("note");
                currenttip.add(I18n.translate("hud.msg.note") + ": " + NOTES[note % 12] + (note / 12 + 1));

                MovingObjectPosition mop = accessor.getPosition();
                Material m = accessor.getWorld().getBlockMaterial(mop.blockX, mop.blockY - 1, mop.blockZ);
                String instrument = "hud.msg.piano";
                if (m == Material.rock) instrument = "hud.msg.bass_drum";
                else if (m == Material.sand) instrument = "hud.msg.snare_drum";
                else if (m == Material.glass) instrument = "hud.msg.clicks_sticks";
                else if (m == Material.wood) instrument = "hud.msg.bass_guitar";
                currenttip.add(I18n.translate("hud.msg.instrument") + ": " + I18n.translate(instrument));
            }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
    }

}
