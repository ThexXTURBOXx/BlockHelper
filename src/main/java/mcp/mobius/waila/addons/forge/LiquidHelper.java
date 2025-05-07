package mcp.mobius.waila.addons.forge;

import java.util.Map;
import java.util.logging.Level;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.StringUtils;
import net.minecraft.nbt.NBTTagCompound;
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

    public static void writeToNBT(ITankContainer container, NBTTagCompound tag) {
        ILiquidTank tank = LiquidHelper.getTank(container);
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
