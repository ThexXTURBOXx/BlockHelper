package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.liquids.ILiquidTank;
import buildcraft.api.liquids.ITankContainer;
import buildcraft.api.liquids.LiquidDictionary;
import buildcraft.api.liquids.LiquidStack;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import buildcraft.factory.TilePump;
import buildcraft.transport.TileGenericPipe;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.util.Map;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

import static net.minecraft.src.mod_BlockHelper.getItemDisplayName;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "buildcraft.energy.TileEngine")) {
            Engine engine = ((TileEngine) state.te).engine;
            if (engine != null && engine.maxEnergy != 0) {
                info.add(engine.energy + " MJ / " + engine.maxEnergy + " MJ");
            }
        } else if (iof(state.te, "buildcraft.api.power.IPowerReceptor")) {
            IPowerProvider prov = ((IPowerReceptor) state.te).getPowerProvider();
            if (prov != null && prov.getMaxEnergyStored() != 0) {
                info.add(prov.getEnergyStored() + " MJ / " + prov.getMaxEnergyStored() + " MJ");
            }
        }
        if (iof(state.te, "buildcraft.api.liquids.ITankContainer")) {
            ITankContainer container = (ITankContainer) state.te;
            for (ILiquidTank tank : container.getTanks()) {
                LiquidStack stack = tank.getLiquid();
                if (tank.getCapacity() != 0 && stack != null && stack.amount > 0) {
                    info.add(stack.amount + " mB / " + tank.getCapacity() + " mB"
                            + formatLiquidName(getBcLiquidName(stack)));
                }
            }
        }
        if (iof(state.te, "buildcraft.factory.TilePump")) {
            TilePump pump = (TilePump) state.te;
            info.add(pump.internalLiquid + " mB / 1000 mB"
                    + formatLiquidName(getBcLiquidName(pump.liquidId)));
        }
    }

    @Override
    public String getMod(BlockHelperBlockState state) {
        if (iof(state.te, "buildcraft.transport.TileGenericPipe")) {
            return "BuildCraft";
        }
        return super.getMod(state);
    }

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "buildcraft.transport.TileGenericPipe")) {
            TileGenericPipe pipe = (TileGenericPipe) state.te;
            if (pipe.pipe != null) {
                return new ItemStack(Item.itemsList[pipe.pipe.itemID], state.te.blockMetadata);
            }
        }
        return super.getItemStack(state);
    }

    private static Map<String, Object> liquids;

    public static String formatLiquidName(String liquidName) {
        return liquidName == null || liquidName.trim().isEmpty()
                ? "" : " of " + liquidName;
    }

    public static String getBcLiquidName(Object liquid) {
        try {
            if (liquids == null) {
                liquids = getDeclaredField(LiquidDictionary.class, null, "liquids");
            }
            LiquidStack stack = (LiquidStack) liquid;
            for (String name : liquids.keySet()) {
                try {
                    LiquidStack stackOfList = (LiquidStack) liquids.get(name);
                    if (stack.isLiquidEqual(stackOfList)) {
                        return name;
                    }
                } catch (Throwable ignored) {
                }
            }
        } catch (Throwable ignored) {
        }
        try {
            ItemStack is = ((LiquidStack) liquid).asItemStack();
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
