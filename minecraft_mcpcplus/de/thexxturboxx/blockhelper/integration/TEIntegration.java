package de.thexxturboxx.blockhelper.integration;

import cofh.thermalexpansion.factory.TileFactoryPowered;
import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.server.TileEntity;

public class TEIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "cofh.thermalexpansion.factory.TileFactoryPowered")) {
            info.add(1, ((TileFactoryPowered) te).getPowerProvider().energyStored + " MJ / "
                    + ((TileFactoryPowered) te).getPowerProvider().maxEnergyStored + " MJ");
        }
    }

}
