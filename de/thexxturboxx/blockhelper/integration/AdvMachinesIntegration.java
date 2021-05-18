package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import ic2.snyke7.advMachine.TileAdvMachine;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "ic2.snyke7.advMachine.TileAdvMachine")) {
            TileAdvMachine tam = ((TileAdvMachine) state.te);
            int energy = Ic2Integration.getRealEnergy(tam.energy, TileAdvMachine.maxEnergy, TileAdvMachine.maxInput);
            info.add(energy + " EU / " + TileAdvMachine.maxEnergy + " EU");
        }
    }

}
