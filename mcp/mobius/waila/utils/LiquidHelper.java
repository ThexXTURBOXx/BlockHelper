package mcp.mobius.waila.utils;

import java.util.Map;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class LiquidHelper {

    public static String getLiquidName(LiquidStack liquidStack) {
        Map<String, LiquidStack> map = LiquidDictionary.getLiquids();
        for (String name : map.keySet()) {
            if (name == null) continue;
            LiquidStack stack = map.get(name);
            if (stack != null && stack.isLiquidEqual(liquidStack)) {
                return firstCharacterUppercase(name);
            }
        }
        return "Unknown";
    }

    private static String firstCharacterUppercase(String str) {
        if (str == null) return null;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

}
