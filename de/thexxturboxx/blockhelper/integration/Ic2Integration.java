package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import ic2.api.IEnergyStorage;
import ic2.common.Ic2Items;
import ic2.common.TileEntityBaseGenerator;
import ic2.common.TileEntityElecMachine;
import ic2.common.TileEntityElectricBlock;
import ic2.common.TileEntityMatter;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;

public class Ic2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "ic2.common.TileEntityElecMachine")) {
            TileEntityElecMachine elecMachine = (TileEntityElecMachine) state.te;
            int energy = getRealEnergy(elecMachine.energy, elecMachine.maxEnergy, elecMachine.maxInput);
            if (elecMachine.maxEnergy != 0) {
                info.add(energy + " EU / " + elecMachine.maxEnergy + " EU");
            }
            if (iof(state.te, "ic2.common.TileEntityMatter")) {
                info.add(I18n.format(state.translator, "progress_format",
                        ((TileEntityMatter) state.te).getProgressAsString()));
            }
        }
        if (iof(state.te, "ic2.api.IEnergyStorage")) {
            IEnergyStorage storage = (IEnergyStorage) state.te;
            if (storage.getCapacity() != 0) {
                info.add(storage.getStored() + " EU / " + storage.getCapacity() + " EU");
            }
        } else if (iof(state.te, "ic2.common.TileEntityBaseGenerator")) {
            TileEntityBaseGenerator generator = (TileEntityBaseGenerator) state.te;
            if (generator.maxStorage != 0) {
                info.add(generator.storage + " EU / " + generator.maxStorage + " EU");
            }
        } else if (iof(state.te, "ic2.common.TileEntityElectricBlock")) {
            TileEntityElectricBlock electricBlock = (TileEntityElectricBlock) state.te;
            if (electricBlock.maxStorage != 0) {
                info.add(electricBlock.energy + " EU / " + electricBlock.maxStorage + " EU");
            }
        }
    }

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "ic2.common.TileEntityCable")) {
            return new ItemStack(Item.itemsList[Ic2Items.copperCableItem.itemID], 1, state.meta);
        }
        return super.getItemStack(state);
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.ic2Integration;
    }

    static int getRealEnergy(int energy, int maxEnergy, int input) {
        return Math.min(maxEnergy, (maxEnergy * energy) / (maxEnergy - input));
    }

}
