package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import ic2.api.IEnergyStorage;
import ic2.core.Ic2Items;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.block.machine.tileentity.TileEntityElecMachine;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class Ic2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "ic2.core.block.machine.tileentity.TileEntityElecMachine")) {
            TileEntityElecMachine elecMachine = (TileEntityElecMachine) state.te;
            info.add(elecMachine.energy + " EU / " + elecMachine.maxEnergy + " EU");
            if (iof(state.te, "ic2.core.block.machine.tileentity.TileEntityMatter")) {
                info.add("Progress: " + ((TileEntityMatter) state.te).getProgressAsString());
            }
        }
        if (iof(state.te, "ic2.api.IEnergyStorage")) {
            IEnergyStorage storage = (IEnergyStorage) state.te;
            info.add(storage.getStored() + " EU / " + storage.getCapacity() + " EU");
        } else if (iof(state.te, "ic2.core.block.generator.tileentity.TileEntityBaseGenerator")) {
            TileEntityBaseGenerator generator = (TileEntityBaseGenerator) state.te;
            info.add(generator.storage + " EU / " + generator.maxStorage + " EU");
        } else if (iof(state.te, "ic2.core.block.wiring.TileEntityElectricBlock")) {
            TileEntityElectricBlock electricBlock = (TileEntityElectricBlock) state.te;
            info.add(electricBlock.energy + " EU / " + electricBlock.maxStorage + " EU");
        }
    }

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "ic2.core.block.wiring.TileEntityCable")) {
            return new ItemStack(Item.itemsList[Ic2Items.copperCableItem.itemID], 1, state.meta);
        }
        return super.getItemStack(state);
    }

}
