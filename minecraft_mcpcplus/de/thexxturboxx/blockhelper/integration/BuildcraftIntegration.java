package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.ILiquidContainer;
import buildcraft.api.IPowerReceptor;
import buildcraft.api.LiquidSlot;
import buildcraft.api.PowerProvider;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import buildcraft.factory.TilePump;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.lang.reflect.Method;
import net.minecraft.server.ItemStack;

import static net.minecraft.server.mod_BlockHelper.getItemDisplayName;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "buildcraft.energy.TileEngine")) {
            Engine engine = ((TileEngine) state.te).engine;
            if (engine != null) {
                Object energyStored = getField(engine, "energy");
                Object maxEnergyStored = getField(engine, "maxEnergy");
                if (energyStored != null) {
                    info.add(energyStored + " MJ / " + maxEnergyStored + " MJ");
                }
            }
        } else if (iof(state.te, "buildcraft.api.IPowerReceptor")) {
            PowerProvider prov = ((IPowerReceptor) state.te).getPowerProvider();
            if (prov != null) {
                Object energyStored = getField(prov, "energyStored");
                Object maxEnergyStored = getField(prov, "maxEnergyStored");
                if (energyStored != null) {
                    info.add(energyStored + " MJ / " + maxEnergyStored + " MJ");
                }
            }
        }
        if (iof(state.te, "buildcraft.api.ILiquidContainer")) {
            ILiquidContainer container = ((ILiquidContainer) state.te);
            Method m = getMethod(state.te, "getLiquidSlots");
            boolean flag = false;
            if (m != null) {
                try {
                    LiquidSlot[] slots = (LiquidSlot[]) m.invoke(state.te);
                    for (LiquidSlot slot : slots) {
                        int quantity = slot.getLiquidQty();
                        int capacity = Math.max(quantity, slot.getCapacity());
                        if (quantity > 0) {
                            info.add(quantity + " mB / " + capacity + " mB"
                                    + formatLiquidName(getBcLiquidName(slot)));
                        }
                    }
                    flag = true;
                } catch (Throwable ignored) {
                }
            }
            if (!flag) {
                int quantity = container.getLiquidQuantity();
                int capacity = Math.max(quantity, container.getCapacity());
                if (quantity > 0) {
                    info.add(quantity + " mB / " + capacity + " mB"
                            + formatLiquidName(getBcLiquidName(container)));
                }
            }
        }
        if (iof(state.te, "buildcraft.factory.TilePump")) {
            TilePump pump = (TilePump) state.te;
            info.add(pump.internalLiquid + " mB / 1000 mB"
                    + formatLiquidName(getBcLiquidName(pump.liquidId)));
        }
    }

    public static String formatLiquidName(String liquidName) {
        return liquidName == null || liquidName.trim().isEmpty()
                ? "" : " of " + liquidName;
    }

    public static String getBcLiquidName(Object liquid) {
        try {
            LiquidSlot slot = (LiquidSlot) liquid;
            ItemStack is = new ItemStack(slot.getLiquidId(), 1, 0);
            return getItemDisplayName(is);
        } catch (Throwable ignored) {
        }
        try {
            ILiquidContainer container = (ILiquidContainer) liquid;
            ItemStack is = new ItemStack(container.getLiquidId(), 1, 0);
            return getItemDisplayName(is);
        } catch (Throwable ignored) {
        }
        try {
            ItemStack is = new ItemStack((Integer) liquid, 1, 0);
            return getItemDisplayName(is);
        } catch (Throwable ignored) {
        }
        return null;
    }

}
