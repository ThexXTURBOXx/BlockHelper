package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import ic2.api.IEnergyStorage;
import ic2.common.Ic2Items;
import ic2.common.TileEntityElecMachine;
import ic2.common.TileEntityElectricBlock;
import ic2.common.TileEntityMatter;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

public class Ic2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "ic2.common.TileEntityElecMachine")) {
            TileEntityElecMachine teem = ((TileEntityElecMachine) te);
            int energy = teem.energy;
            int maxEnergy = teem.maxEnergy;
            int input = teem.maxInput;
            int newEnergy = getRealEnergy(energy, maxEnergy, input);
            info.add(newEnergy + " EU / " + maxEnergy + " EU");
            if (iof(te, "ic2.common.TileEntityMatter")) {
                info.add("Progress: " + ((TileEntityMatter) te).getProgressAsString());
            }
        }
        if (iof(te, "ic2.api.IEnergyStorage")) {
            IEnergyStorage storage = ((IEnergyStorage) te);
            info.add(storage.getStored() + " EU / " + storage.getCapacity() + " EU");
        } else if (iof(te, "ic2.common.TileEntityElectricBlock")) {
            TileEntityElectricBlock teeb = (TileEntityElectricBlock) te;
            info.add(teeb.energy + " EU / " + teeb.maxStorage + " EU");
        }
    }

    @Override
    public ItemStack getItemStack(Block block, TileEntity te, int id, int meta) {
        if (iof(te, "ic2.common.TileEntityCable")) {
            return new ItemStack(Item.itemsList[Ic2Items.copperCableItem.itemID], 1, meta);
        }
        return super.getItemStack(block, te, id, meta);
    }

    static int getRealEnergy(int energy, int maxEnergy, int input) {
        return Math.min(maxEnergy, (maxEnergy * energy) / (maxEnergy - input));
    }

}
