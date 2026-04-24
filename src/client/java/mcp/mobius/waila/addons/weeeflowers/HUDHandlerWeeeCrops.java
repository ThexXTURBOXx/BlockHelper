package mcp.mobius.waila.addons.weeeflowers;

import mcp.mobius.waila.addons.core.HUDHandlerBlocks;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerWeeeCrops implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerWeeeCrops();

    private HUDHandlerWeeeCrops() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return getFlowerCropItem(accessor);
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        currenttip.replaceFirstTagEntry(WHITE + DisplayUtil.itemDisplayNameShort(getFlowerCropItem(accessor)),
                HUDHandlerBlocks.BLOCK_NAME_TAG);
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

    private ItemStack getFlowerCropItem(IDataAccessor accessor) {
        MovingObjectPosition mop = accessor.getPosition();
        return (ItemStack) accessor.getBlock().getBlockDropped(accessor.getWorld(),
                mop.blockX, mop.blockY, mop.blockZ, 7, -10).get(0);
    }

}
