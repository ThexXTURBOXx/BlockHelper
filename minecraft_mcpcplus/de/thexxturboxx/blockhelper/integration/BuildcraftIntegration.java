package de.thexxturboxx.blockhelper.integration;

import buildcraft.energy.TileEngine;
import buildcraft.factory.TileMachine;
import buildcraft.factory.TileTank;
import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.server.TileEntity;

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
        if (iof(te, "buildcraft.energy.TileEngine")) {
            if (((TileEngine) te).engine != null) {
                info.add(1, ((TileEngine) te).engine.energy + " MJ / "
                        + ((TileEngine) te).engine.maxEnergy + " MJ");
            }
        } else if (iof(te, "buildcraft.factory.TileMachine")) {
            info.add(1, ((TileMachine) te).getPowerProvider().energyStored + " MJ / "
                    + ((TileMachine) te).getPowerProvider().maxEnergyStored + " MJ");
        } else if (iof(te, "buildcraft.factory.TileTank")) {
            if (((TileTank) te).stored > 0) {
                if (CAPACITY) {
                    info.add(1, ((TileTank) te).stored + " mB / "
                            + ((TileTank) te).getCapacity() + " mB");
                } else {
                    info.add(1, ((TileTank) te).stored + " mB / "
                            + ((TileTank) te).getTankCapacity() + " mB");
                }
            } else {
                if (CAPACITY) {
                    info.add(1, "0 mB / " + ((TileTank) te).getCapacity() + " mB");
                } else {
                    info.add(1, "0 mB / " + ((TileTank) te).getTankCapacity() + " mB");
                }
            }
        }
    }

}
