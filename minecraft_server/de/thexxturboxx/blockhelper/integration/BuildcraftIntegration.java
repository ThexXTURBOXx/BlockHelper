package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.LiquidSlot;
import buildcraft.api.PowerProvider;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperState;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperState state, InfoHolder info) {
        if (iof(state.te, "buildcraft.energy.TileEngine")) {
            Engine engine = ((TileEngine) state.te).engine;
            if (engine != null) {
                info.add(engine.energy + " MJ / " + engine.maxEnergy + " MJ");
            }
        } else if (iof(state.te, "buildcraft.api.IPowerReceptor")) {
            PowerProvider prov = ((IPowerReceptor) state.te).getPowerProvider();
            if (prov != null) {
                // For some reason (ClassLoader issue?), we need to use reflection here...
                float energyStored = getField(prov, "energyStored");
                float maxEnergyStored = getField(prov, "maxEnergyStored");
                info.add(energyStored + " MJ / " + maxEnergyStored + " MJ");
            }
        }
        if (iof(state.te, "buildcraft.api.ILiquidContainer")) {
            ILiquidContainer container = ((ILiquidContainer) state.te);
            Method m = getMethod(state.te, "getLiquidSlots");
            boolean flag = false;
            if (m != null) {
                LiquidSlot[] slots;
                try {
                    slots = (LiquidSlot[]) m.invoke(state.te);
                    for (LiquidSlot slot : slots) {
                        int quantity = slot.getLiquidQty();
                        int capacity = Math.max(quantity, slot.getCapacity());
                        if (capacity > 0 || quantity > 0) {
                            info.add(quantity + " mB / " + capacity + " mB");
                        }
                        // TODO: Read liquid name from bucket from liquid ??? (BC3 uses BuildcraftAPI class)
                        // Do something here with API.liquids in the near future?
                    }
                    flag = true;
                } catch (IllegalAccessException ignored) {
                } catch (InvocationTargetException ignored) {
                }
            }
            if (!flag) {
                int quantity = container.getLiquidQuantity();
                int capacity = Math.max(quantity, container.getCapacity());
                if (capacity > 0 || quantity > 0) {
                    info.add(quantity + " mB / " + capacity + " mB");
                }
            }
        }
    }

}
