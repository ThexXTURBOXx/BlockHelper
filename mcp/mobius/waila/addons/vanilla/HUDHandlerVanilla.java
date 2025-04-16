package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.utils.LangUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneOre;
import net.minecraft.block.BlockStep;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemRecord;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.world.World;

import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.anvil;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.comparatorAct;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.comparatorIdl;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.crops;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.jukebox;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.leave;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.lever;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.log;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.melonStem;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.mobSpawner;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.pumpkinStem;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.quartz;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.redstone;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.repeaterActv;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.repeaterIdle;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.sapling;
import static mcp.mobius.waila.addons.vanilla.VanillaPlugin.silverfish;

public final class HUDHandlerVanilla implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerVanilla();

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
        if (config.get("vanilla.leverstate"))
            if (block == lever) {
                String redstoneOn = (accessor.getMetadata() & 8) == 0 ? LangUtil.translateG("hud.msg.off") :
                        LangUtil.translateG("hud.msg.on");
                currenttip.add(LangUtil.translateG("hud.msg.state") + " : " + redstoneOn);
            }

        if (config.get("vanilla.repeater"))
            if ((block == repeaterIdle) || (block == repeaterActv)) {
                int tick = (accessor.getMetadata() >> 2) + 1;
                if (tick == 1)
                    currenttip.add(LangUtil.translateG("hud.msg.delay") + " : 1 tick");
                else
                    currenttip.add(LangUtil.translateG("hud.msg.delay") + " : " + tick + " ticks");
            }

        if (config.get("vanilla.comparator"))
            if ((block == comparatorIdl) || (block == comparatorAct)) {
                String mode = ((accessor.getMetadata() >> 2) & 1) == 0
                        ? LangUtil.translateG("hud.msg.comparator")
                        : LangUtil.translateG("hud.msg.subtractor");
                int outputSignal = accessor.getNBTInteger("OutputSignal");
                currenttip.add("Mode : " + mode);
                currenttip.add("Out : " + outputSignal);
            }

        if (config.get("vanilla.redstone"))
            if (block == redstone) {
                currenttip.add(LangUtil.translateG("hud.msg.power") + " : " + accessor.getMetadata());
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
                        : ((ItemRecord) record).getRecordTitle());
            }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world,
                                 int x, int y, int z) {
        if (te != null)
            te.writeToNBT(tag);
    }

}
