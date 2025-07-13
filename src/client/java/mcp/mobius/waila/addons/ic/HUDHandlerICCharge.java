package mcp.mobius.waila.addons.ic;

import java.lang.reflect.Field;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

import static mcp.mobius.waila.api.SpecialChars.ALIGNRIGHT;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.TAB;
import static mcp.mobius.waila.api.SpecialChars.WHITE;


public class HUDHandlerICCharge implements IDataProvider {

    private final Field currCharge;
    private final Field maxCharge;

    public HUDHandlerICCharge(Field currCharge, Field maxCharge) {
        this.currCharge = currCharge;
        this.maxCharge = maxCharge;
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
        if (config.get("ic.storage"))
            try {
                int storage = accessor.getNBTInteger("storage");
                int maxStorage = accessor.getNBTInteger("maxStorage");

                String storedStr = I18n.translate("hud.msg.stored");

                currenttip.add(storedStr + TAB + ALIGNRIGHT + WHITE + Math.min(storage, maxStorage) +
                               (maxStorage > 0 ? RESET + " / " + WHITE + maxStorage : "") + RESET + " EU");
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
            tag.setInteger("storage", currCharge.getInt(te));
            if (maxCharge != null)
                tag.setInteger("maxStorage", maxCharge.getInt(te));
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
        }
    }

}
