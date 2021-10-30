package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import ic2.advancedmachines.TileEntityBaseMachine;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "ic2.advancedmachines.TileEntityBaseMachine")) {
            TileEntityBaseMachine tebm = (TileEntityBaseMachine) state.te;
            int energy = tebm.energy;
            int maxEnergy = tebm.maxEnergy;
            int input = tebm.maxInput;
            int newEnergy = Ic2Integration.getRealEnergy(energy, maxEnergy, input);
            if (maxEnergy != 0) {
                info.add(newEnergy + " EU / " + maxEnergy + " EU");
            }
        }
    }

}
