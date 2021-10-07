package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import ic2.advancedmachines.common.TileEntityBaseMachine;
import ic2.snyke7.advMachine.TileAdvMachine;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "ic2.snyke7.advMachine.TileAdvMachine")) {
            TileAdvMachine tam = (TileAdvMachine) state.te;
            if (TileAdvMachine.maxEnergy != 0) {
                info.add(tam.energy + " EU / " + TileAdvMachine.maxEnergy + " EU");
            }
        }
        if (iof(state.te, "ic2.advancedmachines.common.TileEntityBaseMachine")) {
            TileEntityBaseMachine tebm = (TileEntityBaseMachine) state.te;
            if (tebm.maxEnergy != 0) {
                info.add(tebm.energy + " EU / " + tebm.maxEnergy + " EU");
            }
        }
    }

}
