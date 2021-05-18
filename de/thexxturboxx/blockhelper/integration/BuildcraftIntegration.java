package de.thexxturboxx.blockhelper.integration;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.energy.Engine;
import buildcraft.energy.TileEngine;
import buildcraft.transport.TileGenericPipe;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
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
    }

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "buildcraft.transport.TileGenericPipe")) {
            TileGenericPipe pipe = (TileGenericPipe) state.te;
            if (pipe.pipe != null && pipe.initialized) {
                return new ItemStack(Item.itemsList[pipe.pipe.itemID], 1, state.te.blockMetadata);
            }
        }
        return super.getItemStack(state);
    }

    @Override
    public String getMod(BlockHelperBlockState state) {
        if (iof(state.te, "buildcraft.transport.TileGenericPipe")) {
            return "BuildCraft";
        }
        return super.getMod(state);
    }

}
