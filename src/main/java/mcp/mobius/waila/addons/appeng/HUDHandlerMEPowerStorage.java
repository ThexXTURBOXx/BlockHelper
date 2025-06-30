package mcp.mobius.waila.addons.appeng;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class HUDHandlerMEPowerStorage implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerMEPowerStorage();

    private HUDHandlerMEPowerStorage() {
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
            int storage = accessor.getNBTInteger("AEStorage");
            int maxStorage = accessor.getNBTInteger("AEMaxStorage");

            String storedStr = I18n.translate("hud.msg.stored");

            /* AE Storage */
            if (config.get("appeng.storage")) {
                if (maxStorage > 0)
                    currenttip.add(storedStr + TAB + ALIGNRIGHT + WHITE + Math.min(storage, maxStorage) +
                                   RESET + " / " + WHITE + maxStorage + RESET + " AE");
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
            float storage = -1;
            float maxStorage = -1;

            if (AppEngPlugin.TilePoweredBase.isInstance(te)) {
                storage = AppEngPlugin.TilePoweredBase_storedPower.getFloat(te);
                maxStorage = AppEngPlugin.TilePoweredBase_maxStoredPower.getFloat(te);
            }

            tag.setInteger("AEStorage", Math.round(storage));
            tag.setInteger("AEMaxStorage", Math.round(maxStorage));
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}
