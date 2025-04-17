package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.utils.LangUtil;
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
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.comparatorAct;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.comparatorIdl;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.crops;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.flowerPot;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.jukebox;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.leave;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.lever;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.log;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.melonStem;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.mobSpawner;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.noteBlock;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.pumpkinStem;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.quartz;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.redstone;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.repeaterActv;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.repeaterIdle;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.sapling;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.silverfish;

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

        if (block == silverfish && config.get("vanilla.silverfish")) {
            switch (meta) {
            case 0:
                return new ItemStack(Block.stone);
            case 1:
                return new ItemStack(Block.cobblestone);
            case 2:
                return new ItemStack(Block.brick);
            default:
                return null;
            }
        }

        if (block == redstone) {
            return new ItemStack(Item.redstone);
        }

        if (block instanceof BlockRedstoneOre) {
            return new ItemStack(Block.oreRedstone);
        }

        if (block == crops) {
            return new ItemStack(Item.wheat);
        }

        if (block == leave && (meta > 3)) {
            return new ItemStack(block, 1, meta - 4);
        }

        if (block == log) {
            return new ItemStack(block, 1, meta % 4);
        }

        if ((block == quartz) && (meta > 2)) {
            return new ItemStack(block, 1, 2);
        }

        if (block == anvil ||
            block == sapling ||
            block instanceof BlockStep || block instanceof BlockWoodSlab) {
            return new ItemStack(block, 1, block.damageDropped(meta));
        }

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
            String mobname = ((TileEntityMobSpawner) accessor.getTileEntity()).func_98049_a().getEntityNameToSpawn();
            currenttip.set(0, name + " (" + mobname + ")");
        }

        if (block == redstone) {
            String name = currenttip.get(0).replaceFirst(" " + accessor.getMetadata(), "");
            currenttip.set(0, name);
        }

        if (block == melonStem) {
            currenttip.set(0, SpecialChars.WHITE + "Melon stem");
        }

        if (block == pumpkinStem) {
            currenttip.set(0, SpecialChars.WHITE + "Pumpkin stem");
        }
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();
        int meta = accessor.getMetadata();

        if (config.get("vanilla.leverstate"))
            if (block == lever) {
                String redstoneOn = (meta & 8) == 0 ? LangUtil.translateG("hud.msg.off") :
                        LangUtil.translateG("hud.msg.on");
                currenttip.add(LangUtil.translateG("hud.msg.state") + " : " + redstoneOn);
            }

        if (config.get("vanilla.repeater"))
            if ((block == repeaterIdle) || (block == repeaterActv)) {
                int tick = ((meta & 0xc) >> 2) + 1;
                if (tick == 1)
                    currenttip.add(LangUtil.translateG("hud.msg.delay") + " : 1 tick");
                else
                    currenttip.add(LangUtil.translateG("hud.msg.delay") + " : " + tick + " ticks");
            }

        if (config.get("vanilla.comparator"))
            if ((block == comparatorIdl) || (block == comparatorAct)) {
                String mode = (meta & 4) != 0
                        ? LangUtil.translateG("hud.msg.subtractor")
                        : LangUtil.translateG("hud.msg.comparator");
                int outputSignal = accessor.getNBTInteger("OutputSignal");
                currenttip.add("Mode : " + mode);
                currenttip.add("Out : " + outputSignal);
            }

        if (config.get("vanilla.redstone"))
            if (block == redstone) {
                currenttip.add(LangUtil.translateG("hud.msg.power") + " : " + meta);
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
                        ? LangUtil.translateG("hud.msg.empty")
                        : LangUtil.translateG("hud.msg.record", ((ItemRecord) record).getRecordTitle()));
            }

        if (config.get("vanilla.flowerpot"))
            if (block == flowerPot) {
                ItemStack flower = BlockFlowerPot.getPlantForMeta(meta);
                if (flower != null)
                    currenttip.add(LangUtil.translateG("hud.msg.flower", flower.getDisplayName()));
            }

        if (config.get("vanilla.skull"))
            if (accessor.getTileEntity() instanceof TileEntitySkull) {
                TileEntitySkull te = (TileEntitySkull) accessor.getTileEntity();
                if (te.getSkullType() == 3) {
                    String playerName = te.getExtraType();
                    if (playerName != null && !playerName.isEmpty())
                        currenttip.add(LangUtil.translateG("hud.msg.head_owner", playerName));
                }
            }

        if (config.get("vanilla.noteblock"))
            if (block == noteBlock) {
                int note = accessor.getNBTInteger("note");
                currenttip.add(LangUtil.translateG("hud.msg.note", NOTES[note % 12] + (note / 12 + 1)));

                MovingObjectPosition mop = accessor.getPosition();
                Material m = accessor.getWorld().getBlockMaterial(mop.blockX, mop.blockY - 1, mop.blockZ);
                String instrument = "hud.msg.piano";
                if (m == Material.rock) instrument = "hud.msg.bass_drum";
                else if (m == Material.sand) instrument = "hud.msg.snare_drum";
                else if (m == Material.glass) instrument = "hud.msg.clicks_sticks";
                else if (m == Material.wood) instrument = "hud.msg.bass_guitar";
                currenttip.add(LangUtil.translateG("hud.msg.instrument", LangUtil.translateG(instrument)));
            }

        if (config.get("vanilla.beacon"))
            if (block == beacon) {
                int level = accessor.getNBTInteger("Levels");
                int primary = accessor.getNBTInteger("Primary");
                int secondary = accessor.getNBTInteger("Secondary");
                if (level >= 0)
                    currenttip.add(LangUtil.translateG("hud.msg.level", level));
                if (primary > 0)
                    currenttip.add(LangUtil.translateG("hud.msg.primary_effect",
                            LangUtil.translateG(Potion.potionTypes[primary].getName())));
                if (secondary > 0)
                    currenttip.add(LangUtil.translateG("hud.msg.secondary_effect",
                            LangUtil.translateG(Potion.potionTypes[secondary].getName())));
            }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        if (te != null)
            te.writeToNBT(tag);
    }

}
