package mcp.mobius.waila.addons.appeng;

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

import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class HUDAppEngMonitor implements IDataProvider {

    public static final HUDAppEngMonitor INSTANCE = new HUDAppEngMonitor();

    private HUDAppEngMonitor() {
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
        try {
            if (config.get("appeng.monitorcontent")) {
                if (accessor.getNBTData().hasKey("AppEngMonitor_Item")) {
                    NBTTagCompound nbt = accessor.getNBTData().getCompoundTag("AppEngMonitor_Item");
                    ItemStack stack = ItemStack.loadItemStackFromNBT(nbt);
                    currenttip.add(I18n.translate("hud.msg.item") + ": " + TAB + WHITE + DisplayUtil.itemDisplayNameShort(stack));
                }
            }
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
        try {
            if (AppEngPlugin.TileStorageMonitor.isInstance(te)) {
                ItemStack stack = (ItemStack) AppEngPlugin.TileStorageMonitor_getItem.invoke(te);
                if (stack != null) {
                    NBTTagCompound stackTag = new NBTTagCompound();
                    stack.writeToNBT(stackTag);
                    tag.setCompoundTag("AppEngMonitor_Item", stackTag);
                }
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}
