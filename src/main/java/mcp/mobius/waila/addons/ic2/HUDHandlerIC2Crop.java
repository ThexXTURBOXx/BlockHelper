package mcp.mobius.waila.addons.ic2;

import mcp.mobius.waila.addons.core.DefaultCropProvider;
import mcp.mobius.waila.api.ICropProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.addons.ic2.IC2Plugin.CropCard_getGain;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.CropCard_maxSize;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.CropCard_name;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityCrop_crop;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityCrop_id;
import static mcp.mobius.waila.addons.ic2.IC2Plugin.TileEntityCrop_size;

public class HUDHandlerIC2Crop extends DefaultCropProvider implements IDataProvider {

    public static final IDataProvider DATA_PROVIDER = new HUDHandlerIC2Crop();
    public static final ICropProvider CROP_PROVIDER = (ICropProvider) DATA_PROVIDER; // just use same instance...

    private HUDHandlerIC2Crop() {
        super(0);
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        if (config.get("ic2.crop"))
            try {
                if (TileEntityCrop_id.getInt(accessor.getTileEntity()) < 0) return;
                Object card = TileEntityCrop_crop.invoke(accessor.getTileEntity());
                ItemStack gain = (ItemStack) CropCard_getGain.invoke(card, accessor.getTileEntity());
                String name = gain == null ? (String) CropCard_name.invoke(card)
                        : DisplayUtil.itemDisplayNameShortUnformatted(gain);
                name = name == null || name.isEmpty() ? (String) CropCard_name.invoke(card) : name;
                currenttip.add(I18n.translate("hud.msg.crop") + ": " + name);
            } catch (Throwable t) {
                WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), currenttip);
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

    @Override
    public boolean shouldHandle(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        try {
            return TileEntityCrop_id.getInt(accessor.getTileEntity()) >= 0;
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
        return false;
    }

    @Override
    public int getMaxStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        try {
            Object card = TileEntityCrop_crop.invoke(accessor.getTileEntity());
            return (Integer) CropCard_maxSize.invoke(card);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
        return 1;
    }

    @Override
    public int getCurrentStage(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        try {
            return TileEntityCrop_size.getInt(accessor.getTileEntity());
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
        return 0;
    }

}
