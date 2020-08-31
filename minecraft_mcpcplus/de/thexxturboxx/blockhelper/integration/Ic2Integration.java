package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import ic2.common.TileEntityElecMachine;
import ic2.common.TileEntityElectricBlock;
import ic2.common.TileEntityMatter;
import net.minecraft.server.TileEntity;

public class Ic2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "ic2.common.TileEntityElecMachine")) {
            info.add(1, ((TileEntityElecMachine) te).energy + " EU / "
                    + ((TileEntityElecMachine) te).maxEnergy + " EU");
            if (iof(te, "ic2.common.TileEntityMatter")) {
                info.add(4, "Progress: " + ((TileEntityMatter) te).getProgressAsString());
            }
        } else if (iof(te, "ic2.common.TileEntityElectricBlock")) {
            info.add(1, ((TileEntityElectricBlock) te).energy + " EU / "
                    + ((TileEntityElectricBlock) te).maxStorage + " EU");
        }
    }

}
