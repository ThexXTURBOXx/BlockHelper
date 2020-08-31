package de.thexxturboxx.blockhelper.integration;

import buildcraft.energy.TileEngine;
import buildcraft.factory.TileMachine;
import buildcraft.factory.TileTank;
import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.server.TileEntity;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "buildcraft.energy.TileEngine")) {
            if (((TileEngine) te).engine != null) {
                info.add(1, ((TileEngine) te).engine.getEnergyStored() + " MJ / "
                        + ((TileEngine) te).engine.maxEnergy + " MJ");
            }
        } else if (iof(te, "buildcraft.factory.TileMachine")) {
            info.add(1, ((TileMachine) te).getPowerProvider().energyStored + " MJ / "
                    + ((TileMachine) te).getPowerProvider().maxEnergyStored + " MJ");
        } else if (iof(te, "buildcraft.factory.TileTank")) {
            if (((TileTank) te).stored > 0) {
                info.add(1, ((TileTank) te).stored + " mB / "
                        + ((TileTank) te).getTankCapacity() + " mB");
            } else {
                info.add(1, "0 mB / " + ((TileTank) te).getTankCapacity() + " mB");
            }
        }
    }

}
