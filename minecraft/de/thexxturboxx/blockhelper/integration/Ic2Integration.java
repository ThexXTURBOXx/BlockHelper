package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import ic2.common.TileEntityElecMachine;
import ic2.common.TileEntityElectricBlock;
import ic2.common.TileEntityMatter;

public class Ic2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "ic2.common.TileEntityElecMachine")) {
            TileEntityElecMachine teem = (TileEntityElecMachine) state.te;
            int energy = teem.energy;
            int maxEnergy = teem.maxEnergy;
            int input = teem.maxInput;
            int newEnergy = getRealEnergy(energy, maxEnergy, input);
            if (maxEnergy != 0) {
                info.add(newEnergy + " EU / " + maxEnergy + " EU");
            }
            if (iof(state.te, "ic2.common.TileEntityMatter")) {
                info.add(I18n.format("progress_format", ((TileEntityMatter) state.te).getProgressAsString()));
            }
        }
        if (iof(state.te, "ic2.common.TileEntityElectricBlock")) {
            TileEntityElectricBlock teeb = (TileEntityElectricBlock) state.te;
            if (teeb.maxStorage != 0) {
                info.add(teeb.energy + " EU / " + teeb.maxStorage + " EU");
            }
        }
    }

    static int getRealEnergy(int energy, int maxEnergy, int input) {
        return Math.min(maxEnergy, (maxEnergy * energy) / (maxEnergy - input));
    }

}
