package mcp.mobius.waila.addons.harvestcraft;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public final class HUDHandlerPamCrops implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerPamCrops();

    private HUDHandlerPamCrops() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        // Yes, this is more than just ugly... But Pam's code here is more than just ugly as well...
        try {
            Block b = accessor.getBlock();
            String clazz = b.getClass().getName();
            if (clazz.startsWith("pamsmods.common.harvestcraft") &&
                clazz.contains("Pam") && clazz.endsWith("Crop")) {
                return getPamCropItem(accessor);
            }
        } catch (Throwable ignored) {
        }
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
    }

    private ItemStack getPamCropItem(IDataAccessor accessor) {
        MovingObjectPosition mop = accessor.getPosition();
        return (ItemStack) accessor.getBlock().getBlockDropped(accessor.getWorld(),
                mop.blockX, mop.blockY, mop.blockZ, 7, -10).get(0);
    }

}
