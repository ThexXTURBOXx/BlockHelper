package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import forestry.energy.gadgets.MachineGenerator;

public class ForestryIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "forestry.energy.gadgets.MachineGenerator")) {
            MachineGenerator generator = (MachineGenerator) state.te;
            info.add(generator.energyStored + " EU / " + generator.energyMax + " EU");
        }
    }

}
