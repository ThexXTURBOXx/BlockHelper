package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import ic2.advancedmachines.common.TileEntityBaseMachine;
import ic2.snyke7.advMachine.TileAdvMachine;
import net.minecraft.tileentity.TileEntity;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "ic2.snyke7.advMachine.TileAdvMachine")) {
            TileAdvMachine tam = ((TileAdvMachine) te);
            info.add(tam.energy + " EU / " + TileAdvMachine.maxEnergy + " EU");
        }
        if (iof(te, "ic2.advancedmachines.common.TileEntityBaseMachine")) {
            TileEntityBaseMachine tebm = ((TileEntityBaseMachine) te);
            info.add(tebm.energy + " EU / " + tebm.maxEnergy + " EU");
        }
    }

}
