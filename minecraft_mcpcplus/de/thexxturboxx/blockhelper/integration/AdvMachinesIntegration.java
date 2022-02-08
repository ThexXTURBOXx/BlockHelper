package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import ic2.advMachine.TileAdvMachine;
import net.minecraft.server.ic2.advancedmachines.TileEntityBaseMachine;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "ic2.advMachine.TileAdvMachine")) {
            TileAdvMachine tam = (TileAdvMachine) state.te;
            int energy = tam.energy;
            int maxEnergy = TileAdvMachine.maxEnergy; // Always >0
            int input = TileAdvMachine.maxInput;
            int newEnergy = Ic2Integration.getRealEnergy(energy, maxEnergy, input);
            info.add(newEnergy + " EU / " + maxEnergy + " EU");
        }
        if (iof(state.te, "net.minecraft.server.ic2.advancedmachines.TileEntityBaseMachine")) {
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

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.advMachinesIntegration;
    }

}
