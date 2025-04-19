package mcp.mobius.waila.utils;

import java.util.Map;
import java.util.logging.Level;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public final class LiquidHelper {

    private static final ForgeDirection[] DIRECTIONS = ForgeDirection.values();

    private LiquidHelper() {
        throw new UnsupportedOperationException();
    }

    public static ILiquidTank getTank(ITankContainer container) {
        try {
            for (ForgeDirection dir : DIRECTIONS) {
                ILiquidTank[] tanks = container.getTanks(dir);
                if (tanks != null && tanks.length > 0 && tanks[0] != null)
                    return tanks[0];
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.SEVERE,
                    "[Forge] Unhandled exception trying to access a tank for display!\n", t);
        }
        return null;
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
        return I18n.translate("hud.msg.unknown");
    }

}
