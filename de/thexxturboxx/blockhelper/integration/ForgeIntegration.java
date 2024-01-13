package de.thexxturboxx.blockhelper.integration;

import buildcraft.factory.TileTank;
import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.util.StringTranslate;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.liquids.ILiquidTank;
import net.minecraftforge.liquids.ITankContainer;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class ForgeIntegration extends BlockHelperInfoProvider {

    public static final ForgeDirection[] DIRECTIONS = ForgeDirection.values();

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "net.minecraftforge.liquids.ITankContainer")) {
            ITankContainer container = (ITankContainer) state.te;
            for (ILiquidTank tank : getTanks(container)) {
                LiquidStack stack = tank.getLiquid();
                if (tank.getCapacity() != 0 && stack != null && stack.amount > 0) {
                    info.add(stack.amount + " mB / " + tank.getCapacity() + " mB"
                             + formatLiquidName(state.translator, getLiquidName(stack)));
                }
            }
        }
    }

    public static ILiquidTank[] getTanks(ITankContainer container) {
        if (iof(container, "buildcraft.factory.TileTank")) {
            return new ILiquidTank[]{((TileTank) container).tank};
        }
        if (iof(container, "mods.railcraft.common.blocks.machine.alpha.TileCokeOven")
            || iof(container, "ic2.core.block.generator.tileentity.TileEntityGeoGenerator")) {
            return container.getTanks(ForgeDirection.UNKNOWN);
        }
        Set<ILiquidTank> tanks = new HashSet<ILiquidTank>();
        for (ForgeDirection direction : DIRECTIONS) {
            Collections.addAll(tanks, container.getTanks(direction));
        }
        return tanks.toArray(new ILiquidTank[0]);
    }

    public static String formatLiquidName(StringTranslate translator, String liquidName) {
        return liquidName == null || liquidName.trim().isEmpty()
                ? "" : I18n.format(translator, "liquid_format", "", liquidName);
    }

    public static String getLiquidName(LiquidStack liquidStack) {
        Map<String, LiquidStack> map = LiquidDictionary.getLiquids();
        for (String name : map.keySet()) {
            if (map.get(name).isLiquidEqual(liquidStack)) {
                return name;
            }
        }
        return "";
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.forgeIntegration;
    }

}
