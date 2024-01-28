package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import java.lang.reflect.Method;

public class EEIntegration extends BlockHelperInfoProvider {

    private boolean emcInitialised = false;
    private Method getEMC;

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (!emcInitialised) {
            emcInitialised = true;
            getEMC = getMethod(getClass("mod_EE"), "getDamagedAlchemicalValue", int.class, int.class);
        }
        try {
            Integer emc = (Integer) getEMC.invoke(null, state.id, state.meta);
            if (emc > 0) {
                info.add(I18n.format(state.translator, "emc", emc));
            }
        } catch (Throwable ignored) {
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.eeIntegration;
    }

}
