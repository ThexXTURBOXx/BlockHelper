package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import ic2.api.tile.IEnergyStorage;
import ic2.core.block.generator.tileentity.TileEntityBaseGenerator;
import ic2.core.block.machine.tileentity.TileEntityElectricMachine;
import ic2.core.block.machine.tileentity.TileEntityMatter;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.tileentity.TileEntity;

public class Ic2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "ic2.core.block.machine.tileentity.TileEntityElectricMachine")) {
            TileEntityElectricMachine electricMachine = (TileEntityElectricMachine) te;
            info.add(electricMachine.energy + " EU / " + electricMachine.maxEnergy + " EU");
            if (iof(te, "ic2.core.block.machine.tileentity.TileEntityMatter")) {
                info.add("Progress: " + ((TileEntityMatter) te).getProgressAsString());
            }
        }
        if (iof(te, "ic2.api.tile.IEnergyStorage")) {
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

}
