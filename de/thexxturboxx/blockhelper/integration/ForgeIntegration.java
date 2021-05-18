package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
            ITankContainer container = ((ITankContainer) state.te);
            Set<ILiquidTank> tanks = new HashSet<ILiquidTank>();
            for (ForgeDirection direction : DIRECTIONS) {
                for (ILiquidTank tank : container.getTanks(direction)) {
                    if (tanks.contains(tank)) {
                        continue;
                    } else {
                        tanks.add(tank);
                    }

                    if (tank.getLiquid() != null) {
                        String name = getLiquidName(tank.getLiquid().asItemStack().itemID);
                        if (name.isEmpty()) {
                            info.add("0 mB / " + tank.getCapacity() + " mB");
                        } else {
                            info.add(tank.getLiquid().amount + " mB / "
                                    + tank.getCapacity() + " mB of " + name);
                        }
                    } else {
                        info.add("0 mB / " + tank.getCapacity() + " mB");
                    }
                }
            }
        }
    }

    private String getLiquidName(int id) {
        Map<String, LiquidStack> map = LiquidDictionary.getLiquids();
        for (String name : map.keySet()) {
            if (map.get(name).itemID == id)
                return name;
        }
        return "";
    }

}
