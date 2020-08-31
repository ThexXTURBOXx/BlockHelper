package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import ic2.core.block.machine.tileentity.TileEntityElecMachine;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.tileentity.TileEntity;

public class Ic2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "ic2.core.block.machine.tileentity.TileEntityElecMachine")) {
            info.add(1, ((TileEntityElecMachine) te).energy + " EU / "
                    + ((TileEntityElecMachine) te).maxEnergy + " EU");
            if (iof(te, "ic2.core.block.machine.tileentity.TileEntityMatter")) {
                info.add(4, "Progress: " + ((TileEntityMatter) te).getProgressAsString());
            }
        } else if (iof(te, "ic2.core.block.wiring.TileEntityElectricBlock")) {
            info.add(1, ((TileEntityElectricBlock) te).energy + " EU / "
                    + ((TileEntityElectricBlock) te).maxStorage + " EU");
        }
    }

}
