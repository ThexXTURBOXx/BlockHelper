package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import ic2.snyke7.advMachine.TileAdvMachine;
import net.minecraft.src.TileEntity;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "ic2.snyke7.advMachine.TileAdvMachine")) {
            TileAdvMachine tam = ((TileAdvMachine) te);
            int energy = Ic2Integration.getRealEnergy(tam.energy, TileAdvMachine.maxEnergy, TileAdvMachine.maxInput);
            info.add(energy + " EU / " + TileAdvMachine.maxEnergy + " EU");
        }
    }

}
