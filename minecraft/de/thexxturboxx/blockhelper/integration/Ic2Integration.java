package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
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
        if (iof(state.te, "ic2.TileEntityElecMachine")) {
            ic2.TileEntityElecMachine teem = (ic2.TileEntityElecMachine) state.te;
            int energy = teem.energy;
            int maxEnergy = teem.maxEnergy;
            int input = teem.maxInput;
            int newEnergy = getRealEnergy(energy, maxEnergy, input);
            if (maxEnergy != 0) {
                info.add(newEnergy + " EU / " + maxEnergy + " EU");
            }
            if (iof(state.te, "ic2.TileEntityMatter")) {
                info.add(I18n.format("progress_format", ((ic2.TileEntityMatter) state.te).getProgressAsString()));
            }
        }
        if (iof(state.te, "ic2.TileEntityElectricBlock")) {
            ic2.TileEntityElectricBlock teeb = (ic2.TileEntityElectricBlock) state.te;
            if (teeb.maxStorage != 0) {
                info.add(teeb.energy + " EU / " + teeb.maxStorage + " EU");
            }
        }
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

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.ic2Integration;
    }

    static int getRealEnergy(int energy, int maxEnergy, int input) {
        return Math.min(maxEnergy, (maxEnergy * energy) / (maxEnergy - input));
    }

}
