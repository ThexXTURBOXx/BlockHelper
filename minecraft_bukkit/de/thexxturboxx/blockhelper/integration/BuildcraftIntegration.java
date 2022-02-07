package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.IPowerReceptor;
import buildcraft.api.PowerProvider;
import buildcraft.core.ILiquidContainer;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import buildcraft.factory.TilePump;
import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import net.minecraft.server.ItemStack;

import static net.minecraft.server.mod_BlockHelper.getItemDisplayName;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "buildcraft.energy.TileEngine")) {
            Engine engine = ((TileEngine) state.te).engine;
            if (engine != null) {
                Number energyStored = getField(Engine.class, engine, "energy");
                Number maxEnergy = getField(Engine.class, engine, "maxEnergy");
                if (energyStored != null && maxEnergy != null && maxEnergy.doubleValue() != 0) {
                    info.add(energyStored.intValue() + " MJ / " + maxEnergy.intValue() + " MJ");
                }
            }
        } else if (iof(state.te, "buildcraft.api.IPowerReceptor")) {
            PowerProvider prov = ((IPowerReceptor) state.te).getPowerProvider();
            if (prov != null) {
                Number energyStored = getField(PowerProvider.class, prov, "energyStored");
                Number maxEnergy = getField(PowerProvider.class, prov, "maxEnergyStored");
                if (energyStored != null && maxEnergy != null && maxEnergy.doubleValue() != 0) {
                    info.add(energyStored.intValue() + " MJ / " + maxEnergy.intValue() + " MJ");
                }
            }
        }
        if (iof(state.te, "buildcraft.core.ILiquidContainer")) {
            ILiquidContainer container = (ILiquidContainer) state.te;
            int quantity = container.getLiquidQuantity();
            int capacity = Math.max(quantity, container.getCapacity());
            if (capacity != 0 && quantity > 0) {
                info.add(quantity + " mB / " + capacity + " mB"
                        + formatLiquidName(getBcLiquidName(container)));
            }
        }
        if (iof(state.te, "buildcraft.factory.TilePump")) {
            TilePump pump = (TilePump) state.te;
            info.add(pump.internalLiquid + " mB / 1000 mB"
                    + formatLiquidName(getBcLiquidName(pump)));
        }
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.bcIntegration;
    }

    public static String formatLiquidName(String liquidName) {
        return liquidName == null || liquidName.trim().isEmpty()
                ? "" : I18n.format("liquid_format", "", liquidName);
    }

    public static String getBcLiquidName(Object liquid) {
        try {
            ItemStack is = new ItemStack((Integer) liquid, 1, 0);
            return getItemDisplayName(is);
        } catch (Throwable ignored) {
        }
        return null;
    }

}
