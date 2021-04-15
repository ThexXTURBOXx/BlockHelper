package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import ic2.api.IEnergyStorage;
import ic2.core.Ic2Items;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.block.machine.tileentity.TileEntityElecMachine;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class Ic2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "ic2.core.block.machine.tileentity.TileEntityElecMachine")) {
            TileEntityElecMachine elecMachine = (TileEntityElecMachine) te;
            info.add(elecMachine.energy + " EU / " + elecMachine.maxEnergy + " EU");
            if (iof(te, "ic2.core.block.machine.tileentity.TileEntityMatter")) {
                info.add("Progress: " + ((TileEntityMatter) te).getProgressAsString());
            }
        }
        if (iof(te, "ic2.api.IEnergyStorage")) {
            IEnergyStorage storage = (IEnergyStorage) te;
            info.add(storage.getStored() + " EU / " + storage.getCapacity() + " EU");
        } else if (iof(te, "ic2.core.block.generator.tileentity.TileEntityBaseGenerator")) {
            TileEntityBaseGenerator generator = (TileEntityBaseGenerator) te;
            info.add(generator.storage + " EU / " + generator.maxStorage + " EU");
        } else if (iof(te, "ic2.core.block.wiring.TileEntityElectricBlock")) {
            TileEntityElectricBlock electricBlock = (TileEntityElectricBlock) te;
            info.add(electricBlock.energy + " EU / " + electricBlock.maxStorage + " EU");
        }
    }

    @Override
    public ItemStack getItemStack(Block block, TileEntity te, int id, int meta) {
        if (iof(te, "ic2.core.block.wiring.TileEntityCable")) {
            return new ItemStack(Item.itemsList[Ic2Items.copperCableItem.itemID], 1, meta);
        }
        return super.getItemStack(block, te, id, meta);
    }

}
