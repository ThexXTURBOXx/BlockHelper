package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.liquids.ILiquidTank;
import buildcraft.api.liquids.ITankContainer;
import buildcraft.api.liquids.LiquidDictionary;
import buildcraft.api.liquids.LiquidStack;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.src.TileEntity;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "buildcraft.energy.TileEngine")) {
            Engine engine = ((TileEngine) te).engine;
            if (engine != null) {
                info.add(engine.energy + " MJ / " + engine.maxEnergy + " MJ");
            }
        } else if (iof(te, "buildcraft.api.power.IPowerReceptor")) {
            IPowerProvider prov = ((IPowerReceptor) te).getPowerProvider();
            if (prov != null) {
                info.add(prov.getEnergyStored() + " MJ / " + prov.getMaxEnergyStored() + " MJ");
            }
        }
        if (iof(te, "buildcraft.api.liquids.ITankContainer")) {
            ITankContainer container = ((ITankContainer) te);
            Set<ILiquidTank> tanks = new HashSet<ILiquidTank>();
            for (ILiquidTank tank : container.getTanks()) {
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

    private static Map<String, LiquidStack> liquids;

    private String getLiquidName(int id) {
        if (liquids == null) {
            liquids = getDeclaredField(LiquidDictionary.class, "liquids");
        }
        for (String name : liquids.keySet()) {
            if (liquids.get(name).itemID == id)
                return name;
        }
        return "";
    }

}
