package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlowerPot;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.BlockStep;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.MovingObjectPosition;

import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.anvil;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.beacon;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.bed;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.carrot;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.cauldron;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.crops;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.endPortal;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.flowerPot;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.jukebox;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.leave;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.lever;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.log;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.melonStem;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.mobSpawner;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.noteBlock;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.pistonExtension;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.pistonMoving;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.potato;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.pumpkinStem;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.redstone;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.repeaterActv;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.repeaterIdle;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.sapling;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.silverfish;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.sugarCane;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerVanilla implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerVanilla();

    private static final String[] NOTES = {"F\u266F/G\u266D", "G", "G\u266F/A\u266D", "A", "A\u266F/B\u266D", "B",
            "C", "C\u266F/D\u266D", "D", "D\u266F/E\u266D", "E", "F"};

    private HUDHandlerVanilla() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        int meta = accessor.getMetadata();

        if (block == silverfish && config.get("vanilla.silverfish"))
            switch (meta) {
            case 1:
                return new ItemStack(Block.cobblestone);
            case 2:
                return new ItemStack(Block.stoneBrick);
            default:
                return new ItemStack(Block.stone);
            }

        if (block == redstone)
            return new ItemStack(Item.redstone);

        if (block instanceof BlockRedstoneOre)
            return new ItemStack(Block.oreRedstone);

        if (block == repeaterIdle || block == repeaterActv)
            return new ItemStack(Item.redstoneRepeater);

        if (block == melonStem)
            return new ItemStack(Item.melonSeeds);

        if (block == pumpkinStem)
            return new ItemStack(Item.pumpkinSeeds);

        if (block == sugarCane)
            return new ItemStack(Item.reed);

        if (block == crops)
            return new ItemStack(Item.wheat);

        if (block == carrot)
            return new ItemStack(Item.carrot);

        if (block == potato)
            return new ItemStack(Item.potato);

        if (block == flowerPot)
            return new ItemStack(Item.flowerPot);

        if (block == cauldron)
            return new ItemStack(Item.cauldron);

        if (block == bed)
            return new ItemStack(Item.bed);

        if (block == leave && (meta > 3))
            return new ItemStack(block, 1, meta - 4);

        if (block == log)
            return new ItemStack(block, 1, meta % 4);

        if (block == anvil ||
            block == sapling ||
            block instanceof BlockStep || block instanceof BlockWoodSlab)
            return new ItemStack(block, 1, block.damageDropped(meta));

        return null;

    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();

        /* Mob spawner handler */
        if (block == mobSpawner && accessor.getTileEntity() instanceof TileEntityMobSpawner && config.get(
                "vanilla.spawntype")) {
            String name = currenttip.get(0);
            String mobname = ((TileEntityMobSpawner) accessor.getTileEntity()).getMobID();
            currenttip.set(0, name + " (" + mobname + ")");
        }

        if (block == melonStem)
            currenttip.set(0, WHITE + I18n.translate("tile.melonStem.name"));

        if (block == pumpkinStem)
            currenttip.set(0, WHITE + I18n.translate("tile.pumpkinStem.name"));

        if (block == endPortal)
            currenttip.set(0, WHITE + I18n.translate("tile.endPortal.name"));

        if (block == pistonExtension)
            currenttip.set(0, WHITE + I18n.translate("tile.pistonExtension.name"));

        if (block == pistonMoving)
            currenttip.set(0, WHITE + I18n.translate("tile.pistonMoving.name"));
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

                if (tag.hasKey("RecordItem")) {
                    ItemStack stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("RecordItem"));
                    record = stack == null ? null : stack.getItem();
                }

                if (record == null && tag.hasKey("Record"))
                    record = Item.itemsList[accessor.getNBTInteger(tag, "Record")];

                currenttip.add(record == null
                        ? I18n.translate("hud.msg.empty")
                        : (I18n.translate("hud.msg.record") + ": " + ((ItemRecord) record).getRecordTitle()));
            }

        if (config.get("vanilla.flowerpot"))
            if (block == flowerPot) {
                ItemStack flower = BlockFlowerPot.getPlantForMeta(meta);
                if (flower != null)
                    currenttip.add(I18n.translate("hud.msg.flower") + ": " + flower.getDisplayName());
            }

        if (config.get("vanilla.skull"))
            if (accessor.getTileEntity() instanceof TileEntitySkull) {
                TileEntitySkull te = (TileEntitySkull) accessor.getTileEntity();
                if (te.getSkullType() == 3) {
                    String playerName = te.getExtraType();
                    if (playerName != null && !playerName.isEmpty())
                        currenttip.add(I18n.translate("hud.msg.head_owner") + ": " + playerName);
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

        if (config.get("vanilla.beacon"))
            if (block == beacon) {
                int level = accessor.getNBTInteger("Levels");
                int primary = accessor.getNBTInteger("Primary");
                int secondary = accessor.getNBTInteger("Secondary");
                if (level >= 0)
                    currenttip.add(I18n.translate("hud.msg.level") + ": " + level);
                if (primary > 0)
                    currenttip.add(I18n.translate("hud.msg.primary_effect") + ": " +
                                   I18n.translate(Potion.potionTypes[primary].getName()));
                if (secondary > 0)
                    currenttip.add(I18n.translate("hud.msg.secondary_effect") + ": " +
                                   I18n.translate(Potion.potionTypes[secondary].getName()));
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
