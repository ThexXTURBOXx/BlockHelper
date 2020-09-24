package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.LiquidSlot;
import buildcraft.api.PowerProvider;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import net.minecraft.src.TileEntity;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "buildcraft.energy.TileEngine")) {
            Engine engine = ((TileEngine) te).engine;
            if (engine != null) {
                info.add(engine.energy + " MJ / " + engine.maxEnergy + " MJ");
            }
        } else if (iof(te, "buildcraft.api.IPowerReceptor")) {
            PowerProvider prov = ((IPowerReceptor) te).getPowerProvider();
            if (prov != null) {
                info.add(prov.energyStored + " MJ / " + prov.maxEnergyStored + " MJ");
            }
        }
        if (iof(te, "buildcraft.api.ILiquidContainer")) {
            ILiquidContainer container = ((ILiquidContainer) te);
            Method m = getMethod(te, "getLiquidSlots");
            boolean flag = false;
            if (m != null) {
                LiquidSlot[] slots;
                try {
                    slots = (LiquidSlot[]) m.invoke(te);
                    for (LiquidSlot slot : slots) {
                        int quantity = slot.getLiquidQty();
                        int capacity = Math.max(quantity, slot.getCapacity());
                        if (capacity > 0 || quantity > 0) {
                            info.add(quantity + " mB / " + capacity + " mB");
                        }
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
