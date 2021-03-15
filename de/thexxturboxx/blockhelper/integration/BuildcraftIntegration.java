package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.tileentity.TileEntity;

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
    }

}
