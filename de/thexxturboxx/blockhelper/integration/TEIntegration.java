package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import java.util.Map;
import net.minecraft.block.Block;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import thermalexpansion.block.conduit.TileConduitLiquid;
import thermalexpansion.block.device.TileEnergyCell;
import thermalexpansion.block.device.TileTankPortable;
import thermalexpansion.block.engine.TileEngineRoot;
import thermalexpansion.block.machine.TileMachinePower;

public class TEIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(Block b, int id, int meta, InfoHolder info) {
        if (iof(b, "thermalexpansion.block.machine.TileMachinePower")) {
            info.add(1, ((TileMachinePower) b).getPowerProvider().getEnergyStored() + " MJ / "
                    + ((TileMachinePower) b).getPowerProvider().getMaxEnergyStored() + " MJ");
        } else if (iof(b, "thermalexpansion.block.device.TileEnergyCell")) {
            info.add(1, ((TileEnergyCell) b).getPowerProvider().getEnergyStored() + " MJ / "
                    + ((TileEnergyCell) b).getPowerProvider().getMaxEnergyStored() + " MJ");
        } else if (iof(b, "thermalexpansion.block.engine.TileEngineRoot")) {
            info.add(1, ((TileEngineRoot) b).getPowerProvider().getEnergyStored() + " MJ / "
                    + ((TileEngineRoot) b).getPowerProvider().getMaxEnergyStored() + " MJ");
        } else if (iof(b, "thermalexpansion.block.device.TileTankPortable")) {
            if (((TileTankPortable) b).myTank.getLiquid() != null) {
                String name = getLiquidName(
                        ((TileTankPortable) b).myTank.getLiquid().asItemStack().itemID);
                if (name.equals("")) {
                    info.add(1, "0 mB / " + ((TileTankPortable) b).myTank.getCapacity() + " mB");
                } else {
                    info.add(1, ((TileTankPortable) b).getTankLiquid().amount + " mB / "
                            + ((TileTankPortable) b).myTank.getCapacity() + " mB of " + name);
                }
            } else {
                info.add(1, "0 mB / " + ((TileTankPortable) b).myTank.getCapacity() + " mB");
            }
        } else if (iof(b, "thermalexpansion.block.conduit.TileConduitLiquid")) {
            if (((TileConduitLiquid) b).getRenderLiquid() != null) {
                String name = getLiquidName(((TileConduitLiquid) b).liquidID);
                if (name.equals("")) {
                    info.add(1,
                            "0 mB / " + ((TileConduitLiquid) b).myGrid.myTank.getCapacity() + " mB");
                } else {
                    String liquid = ((((TileConduitLiquid) b).myGrid.myTank.getCapacity() / 6.0D)
                            * ((TileConduitLiquid) b).liquidLevel) + "";
                    info.add(1, liquid.substring(0, liquid.indexOf(".")) + " mB / "
                            + ((TileConduitLiquid) b).myGrid.myTank.getCapacity() + " mB of " + name);
                }
            } else {
                info.add(1, "0 mB / " + ((TileConduitLiquid) b).myGrid.myTank.getCapacity() + " mB");
            }
        }
    }

    private String getLiquidName(int id) {
        Map<String, LiquidStack> map = LiquidDictionary.getLiquids();
        for (String name : map.keySet()) {
            if (map.get(name).itemID == id)
                return name;
        }
        return "";
    }

}
