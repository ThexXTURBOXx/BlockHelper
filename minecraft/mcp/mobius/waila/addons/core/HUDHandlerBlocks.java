package mcp.mobius.waila.addons.core;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.Configuration;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.DRED;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.YELLOW;

public final class HUDHandlerBlocks implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerBlocks();

    private HUDHandlerBlocks() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        try {
            String s = DisplayUtil.itemDisplayNameShort(itemStack);
            String name = null;
            if (s != null && !s.endsWith("Unnamed"))
                name = s;

            if (name != null)
                currenttip.add(name);
        } catch (Throwable ignored) {
        }

        if (currenttip.isEmpty())
            currenttip.add(I18n.translate("hud.msg.please_report"));
        if (PluginConfig.instance().get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_METADATA, true))
            currenttip.add(ITALIC + "ID " + accessor.getBlockID() + ":" + accessor.getMetadata());
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        Block b = accessor.getBlock();
        int meta = accessor.getMetadata();
        World w = accessor.getWorld();
        int x = accessor.getPosition().blockX;
        int y = accessor.getPosition().blockY;
        int z = accessor.getPosition().blockZ;

        if (config.get("general.harvest")) {
            String harvest = "hud.msg.please_report";
            if (b != null) {
                if (b.getBlockHardness(w, x, y, z) < 0.0F) {
                    harvest = "hud.msg.unbreakable";
                } else if (b.canHarvestBlock(accessor.getPlayer(), meta)) {
                    harvest = "hud.msg.harvestable";
                } else {
                    harvest = "hud.msg.not_harvestable";
                }
            }
            currenttip.add(I18n.translate(harvest));
        }

        if (config.get("general.lightlevel") &&
            (!w.isBlockNormalCubeDefault(x, y + 1, z, false) || w.isAirBlock(x, y + 1, z))) {
            int blockLightLevel = w.getSavedLightValue(EnumSkyBlock.Block, x, y + 1, z);
            int spawnMode = getSpawnMode(w.getChunkFromBlockCoords(x, z), x, y + 1, z);
            String blockLight = (spawnMode == 0 ? GREEN : (spawnMode == 1 ? YELLOW : DRED)) + blockLightLevel;
            String skyLight = w.getSavedLightValue(EnumSkyBlock.Sky, x, y + 1, z) + "";
            currenttip.add(I18n.translate("hud.msg.light_level") + ": " + blockLight + YELLOW + " (" + skyLight + ")");
        }

        if (config.get("general.break")) {
            try {
                float curBlockDamage = CorePlugin.curBlockDamageMP.getFloat(Minecraft.getMinecraft().playerController);
                if (curBlockDamage > 0) {
                    String progress = MathHelper.floor_float(100 * curBlockDamage) + "%";
                    currenttip.add(I18n.translate("hud.msg.break_progression") + ": " + progress);
                }
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        }
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        String modName = ModIdentification.nameFromStack(itemStack);
        if (!modName.isEmpty())
            currenttip.add(BLUE + ITALIC + modName);
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
        if (te != null)
            te.writeToNBT(tag);
    }

    private byte getSpawnMode(Chunk chunk, int x, int y, int z) {
        if (!SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, chunk.worldObj, x, y, z) ||
            chunk.getSavedLightValue(EnumSkyBlock.Block, x & 0xF, y, z & 0xF) >= 8)
            return 0;
        AxisAlignedBB aabb = AxisAlignedBB.getAABBPool().getAABB(
                x + 0.2, y + 0.01, z + 0.2, x + 0.8, y + 1.8, z + 0.8);
        if (!chunk.worldObj.checkNoEntityCollision(aabb) ||
            !chunk.worldObj.getCollidingBlockBounds(aabb).isEmpty() || chunk.worldObj.isAnyLiquid(aabb))
            return 0;
        if (chunk.getSavedLightValue(EnumSkyBlock.Sky, x & 0xF, y, z & 0xF) >= 8)
            return 1;
        return 2;
    }

}
