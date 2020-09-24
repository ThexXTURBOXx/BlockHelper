package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import ic2.advMachine.TileAdvMachine;
import ic2.advancedmachines.TileEntityBaseMachine;
import net.minecraft.server.TileEntity;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "ic2.advMachine.TileAdvMachine")) {
            TileAdvMachine tam = ((TileAdvMachine) te);
            int energy = tam.energy;
            int maxEnergy = TileAdvMachine.maxEnergy;
            int input = TileAdvMachine.maxInput;
            int newEnergy = Ic2Integration.getRealEnergy(energy, maxEnergy, input);
            info.add(newEnergy + " EU / " + maxEnergy + " EU");
        }
        if (iof(te, "ic2.advancedmachines.TileEntityBaseMachine")) {
            TileEntityBaseMachine tebm = ((TileEntityBaseMachine) te);
            int energy = tebm.energy;
            int maxEnergy = tebm.maxEnergy;
            int input = tebm.maxInput;
            int newEnergy = Ic2Integration.getRealEnergy(energy, maxEnergy, input);
            info.add(newEnergy + " EU / " + maxEnergy + " EU");
        }
    }

}
