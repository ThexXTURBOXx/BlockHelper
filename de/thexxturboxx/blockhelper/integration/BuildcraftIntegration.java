package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.liquids.ILiquidTank;
import buildcraft.api.liquids.ITankContainer;
import buildcraft.api.liquids.LiquidDictionary;
import buildcraft.api.liquids.LiquidStack;
import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import buildcraft.transport.TileGenericPipe;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperState;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperState state, InfoHolder info) {
        if (iof(state.te, "buildcraft.energy.TileEngine")) {
            Engine engine = ((TileEngine) state.te).engine;
            if (engine != null) {
                info.add(engine.energy + " MJ / " + engine.maxEnergy + " MJ");
            }
        } else if (iof(state.te, "buildcraft.api.power.IPowerReceptor")) {
            IPowerProvider prov = ((IPowerReceptor) state.te).getPowerProvider();
            if (prov != null) {
                info.add(prov.getEnergyStored() + " MJ / " + prov.getMaxEnergyStored() + " MJ");
            }
        }
        if (iof(state.te, "buildcraft.api.liquids.ITankContainer")) {
            ITankContainer container = ((ITankContainer) state.te);
            Set<ILiquidTank> tanks = new HashSet<ILiquidTank>();
            for (ILiquidTank tank : container.getTanks()) {
                if (tanks.contains(tank)) {
                    continue;
                } else {
                    tanks.add(tank);
                }

                if (tank.getLiquid() != null) {
                    String name = getLiquidName(tank.getLiquid().asItemStack().itemID);
                    if (name.isEmpty()) {
                        info.add("0 mB / " + tank.getCapacity() + " mB");
                    } else {
                        info.add(tank.getLiquid().amount + " mB / "
                                + tank.getCapacity() + " mB of " + name);
                    }
                } else {
                    info.add("0 mB / " + tank.getCapacity() + " mB");
                }
            }
        }
    }

    @Override
    public String getMod(BlockHelperState state) {
        if (iof(state.te, "buildcraft.transport.TileGenericPipe")) {
            return "BuildCraft";
        }
        return super.getMod(state);
    }

    @Override
    public ItemStack getItemStack(BlockHelperState state) {
        if (iof(state.te, "buildcraft.transport.TileGenericPipe")) {
            TileGenericPipe pipe = (TileGenericPipe) state.te;
            if (pipe.pipe != null) {
                return new ItemStack(Item.itemsList[pipe.pipe.itemID], state.te.blockMetadata);
            }
        }
        return super.getItemStack(state);
    }

    private static Map<String, LiquidStack> liquids;

    private String getLiquidName(int id) {
        if (liquids == null) {
            liquids = getDeclaredField(LiquidDictionary.class, "liquids");
        }
        for (String name : liquids.keySet()) {
            if (liquids.get(name).itemID == id)
                return name;
        }
        return "";
    }

}
