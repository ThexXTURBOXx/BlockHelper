package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.energy.TileEngine;
import buildcraft.factory.TileTank;
import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.src.TileEntity;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    private static boolean loaded = false;
    private static final boolean CAPACITY;

    static {
        boolean cap;
        try {
            Class.forName("buildcraft.factory.TileTank").getMethod("getCapacity");
            cap = true;
            loaded = true;
        } catch (Throwable t) {
            try {
                Class.forName("buildcraft.factory.TileTank").getMethod("getTankCapacity");
                cap = false;
                loaded = true;
            } catch (Throwable t1) {
                cap = true;
            }
        }
        CAPACITY = cap;
    }

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (!loaded) {
            return;
        }
        if (iof(te, "buildcraft.api.IPowerReceptor")) {
            info.add(1, ((IPowerReceptor) te).getPowerProvider().energyStored + " MJ / "
                    + ((IPowerReceptor) te).getPowerProvider().maxEnergyStored + " MJ");
        } else if (iof(te, "buildcraft.energy.TileEngine")) {
            if (((TileEngine) te).engine != null) {
                info.add(2, ((TileEngine) te).engine.energy + " MJ/t");
            }
        } else if (iof(te, "buildcraft.api.ILiquidContainer")) {
            if (((ILiquidContainer) te).getLiquidQuantity() > 0) {
                if (CAPACITY) {
                    info.add(1, ((ILiquidContainer) te).getLiquidQuantity() + " mB / "
                            + ((ILiquidContainer) te).getCapacity() + " mB");
                } else {
                    info.add(1, ((ILiquidContainer) te).getLiquidQuantity() + " mB / "
                            + ((ILiquidContainer) te).getTankCapacity() + " mB");
                }
            } else {
                if (CAPACITY) {
                    info.add(1, "0 mB / " + ((ILiquidContainer) te).getCapacity() + " mB");
                } else {
                    info.add(1, "0 mB / " + ((ILiquidContainer) te).getTankCapacity() + " mB");
                }
            }
        }
    }

}
