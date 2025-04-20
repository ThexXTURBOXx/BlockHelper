package mcp.mobius.waila.addons.harvestcraft;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.addons.harvestcraft.HarvestcraftPlugin.BlockPamCrop;
import static mcp.mobius.waila.addons.harvestcraft.HarvestcraftPlugin.BlockPamCrop_getCropItem;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerPamCrops implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerPamCrops();

    private HUDHandlerPamCrops() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        try {
            Block b = accessor.getBlock();
            if (BlockPamCrop.isInstance(b))
                return new ItemStack((Integer) BlockPamCrop_getCropItem.invoke(b), 1, 0);
        } catch (Throwable ignored) {
        }
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        try {
            Block b = accessor.getBlock();
            if (BlockPamCrop.isInstance(b))
                currenttip.set(0,
                        WHITE + new ItemStack((Integer) BlockPamCrop_getCropItem.invoke(b), 1, 0).getDisplayName());
        } catch (Throwable ignored) {
        }
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

}
