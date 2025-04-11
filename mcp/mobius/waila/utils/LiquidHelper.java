package mcp.mobius.waila.utils;

import java.util.Map;
import java.util.logging.Level;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public final class LiquidHelper {

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    public static ILiquidTank getTank(IDataAccessor accessor) {
        try {
            ILiquidTank[] tanks = ((ITankContainer) accessor.getTileEntity()).getTanks(ForgeDirection.UNKNOWN);
            return tanks.length > 0 ? tanks[0] : null;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.SEVERE,
                    "[Forge] Unhandled exception trying to access a tank for display!\n", t);
            return null;
        }
    }

    public static String getLiquidName(LiquidStack liquidStack) {
        Map<String, LiquidStack> map = LiquidDictionary.getLiquids();
        for (String name : map.keySet()) {
            if (name == null) continue;
            LiquidStack stack = map.get(name);
            if (stack != null && stack.isLiquidEqual(liquidStack)) {
                return StringUtils.firstCharacterUppercase(name);
            }
        }
        return "Unknown";
    }

}
