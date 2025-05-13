package mcp.mobius.waila.addons.bc2;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.LiquidSlot;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.AccessHelper;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_BlockHelper;

public final class LiquidHelper {

    private static Method ILiquidContainer_getCapacity;

    static {
        try {
            ILiquidContainer_getCapacity = AccessHelper.getMethod(ILiquidContainer.class, new Class[0], "getCapacity");
        } catch (Throwable ignored) {
        }
    }

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    public static void writeToNBT(Object container, NBTTagCompound tag) {
        LiquidSlot slot = LiquidHelper.getTank((ILiquidContainer) container);
        int liquidId = slot != null ? slot.getLiquidId() : 0;
        int liquidAmount = slot != null ? slot.getLiquidQty() : 0;
        int capacity = slot != null ? slot.getCapacity() : 0;

        tag.setInteger("liquidtype", liquidId);
        tag.setInteger("liquidamt", liquidAmount);
        tag.setInteger("liquidcapacity", capacity);
    }

    public static LiquidSlot getTank(ILiquidContainer container) {
        try {
            int quantity = container.getLiquidQuantity();
            int capacity = quantity;
            try {
                // Against compile errors
                capacity = Math.max(capacity, ILiquidContainer_getCapacity != null ?
                        (Integer) ILiquidContainer_getCapacity.invoke(container) : 0);
            } catch (Throwable ignored) {
            }
            if (capacity > 0)
                return new LiquidSlot(container.getLiquidId(), quantity, capacity);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.SEVERE,
                    "[BC2] Unhandled exception trying to access a tank for display!\n", t);
        }

        return null;
    }

    public static String getLiquidName(int liquidId) {
        return getLiquidName(new LiquidSlot(liquidId, 1, 1));
    }

    public static String getLiquidName(Object liquidSlotRaw) {
        try {
            LiquidSlot liquidSlot = (LiquidSlot) liquidSlotRaw;
            ItemStack stack = new ItemStack(liquidSlot.getLiquidId(), 1, 0);
            return DisplayUtil.itemDisplayNameShort(stack);
        } catch (Throwable ignored) {
        }

        return I18n.translate("hud.msg.unknown");
    }

}
