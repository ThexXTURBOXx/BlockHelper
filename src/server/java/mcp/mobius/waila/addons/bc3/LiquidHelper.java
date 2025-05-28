package mcp.mobius.waila.addons.bc3;

import buildcraft.api.liquids.ILiquidTank;
import buildcraft.api.liquids.ITankContainer;
import buildcraft.api.liquids.LiquidStack;
import java.util.logging.Level;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.mod_BlockHelper;

public final class LiquidHelper {

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    public static void writeToNBT(Object container, NBTTagCompound tag) {
        ILiquidTank tank = LiquidHelper.getTank((ITankContainer) container);
        LiquidStack stack = tank != null ? tank.getLiquid() : null;
        int capacity = tank != null ? tank.getCapacity() : 0;

        if (stack != null) {
            NBTTagCompound stackNBT = new NBTTagCompound();
            stack.writeToNBT(stackNBT);
            tag.setCompoundTag("liquidstack", stackNBT);
        }
        tag.setInteger("liquidcapacity", capacity);
    }

    public static ILiquidTank getTank(ITankContainer container) {
        try {
            ILiquidTank[] tanks = container.getTanks();
            if (tanks != null && tanks.length > 0 && tanks[0] != null)
                return tanks[0];
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.SEVERE,
                    "[BC3] Unhandled exception trying to access a tank for display!\n", t);
        }
        return null;
    }

}
