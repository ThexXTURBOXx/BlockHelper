package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "TileEntityAdvBlock3")) {
            Class<?> clazz = getClass("TileEntityAdvBlock3");
            Integer energy = getField(clazz, state.te, "energy");
            Integer maxEnergy = getField(clazz, state.te, "maxEnergy");
            Integer input = getField(clazz, state.te, "maxInput");
            if (energy != null && maxEnergy != null && input != null) {
                int newEnergy = Ic2Integration.getRealEnergy(energy, maxEnergy, input);
                info.add(newEnergy + " EU / " + maxEnergy + " EU");
            }
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.advMachinesIntegration;
    }

}
