package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import forestry.TileMachine;
import forestry.energy.MachineGenerator;

public class ForestryIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "forestry.TileMachine")) {
            TileMachine machine = (TileMachine) state.te;
            if (iof(machine.machine, "forestry.energy.MachineGenerator")) {
                MachineGenerator generator = (MachineGenerator) machine.machine;
                if (generator.energyMax != 0) {
                    info.add(generator.energyStored + " EU / " + generator.energyMax + " EU");
                }
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.forestryIntegration;
    }

}
