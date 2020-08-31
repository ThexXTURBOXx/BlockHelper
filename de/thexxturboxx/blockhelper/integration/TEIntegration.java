package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import java.util.Map;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;
import thermalexpansion.energy.tileentity.TileEnergyCell;
import thermalexpansion.energy.tileentity.TileEngineRoot;
import thermalexpansion.factory.tileentity.TileMachinePower;
import thermalexpansion.factory.tileentity.TilePortableTank;
import thermalexpansion.transport.tileentity.TileConduitLiquid;

public class TEIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "thermalexpansion.factory.tileentity.TileMachinePower")) {
            info.add(1, ((TileMachinePower) te).getPowerProvider().getEnergyStored() + " MJ / "
                    + ((TileMachinePower) te).getPowerProvider().getMaxEnergyStored() + " MJ");
        } else if (iof(te, "thermalexpansion.energy.tileentity.TileEnergyCell")) {
            info.add(1, ((TileEnergyCell) te).getPowerProvider().getEnergyStored() + " MJ / "
                    + ((TileEnergyCell) te).getPowerProvider().getMaxEnergyStored() + " MJ");
        } else if (iof(te, "thermalexpansion.energy.tileentity.TileEngineRoot")) {
            info.add(1, ((TileEngineRoot) te).getPowerProvider().getEnergyStored() + " MJ / "
                    + ((TileEngineRoot) te).getPowerProvider().getMaxEnergyStored() + " MJ");
        } else if (iof(te, "thermalexpansion.factory.tileentity.TilePortableTank")) {
            if (((TilePortableTank) te).myTank.getLiquid() != null) {
                String name = getLiquidName(
                        ((TilePortableTank) te).myTank.getLiquid().asItemStack().itemID);
                if (name.equals("")) {
                    info.add(1, "0 mB / " + ((TilePortableTank) te).myTank.getCapacity() + " mB");
                } else {
                    info.add(1, ((TilePortableTank) te).getTankLiquid().amount + " mB / "
                            + ((TilePortableTank) te).myTank.getCapacity() + " mB of " + name);
                }
            } else {
                info.add(1, "0 mB / " + ((TilePortableTank) te).myTank.getCapacity() + " mB");
            }
        } else if (iof(te, "thermalexpansion.transport.tileentity.TileConduitLiquid")) {
            if (((TileConduitLiquid) te).getRenderLiquid() != null) {
                String name = getLiquidName(((TileConduitLiquid) te).liquidID);
                if (name.equals("")) {
                    info.add(1,
                            "0 mB / " + ((TileConduitLiquid) te).myGrid.myTank.getCapacity() + " mB");
                } else {
                    String liquid = ((((TileConduitLiquid) te).myGrid.myTank.getCapacity() / 6.0D)
                            * ((TileConduitLiquid) te).liquidLevel) + "";
                    info.add(1, liquid.substring(0, liquid.indexOf(".")) + " mB / "
                            + ((TileConduitLiquid) te).myGrid.myTank.getCapacity() + " mB of " + name);
                }
            } else {
                info.add(1, "0 mB / " + ((TileConduitLiquid) te).myGrid.myTank.getCapacity() + " mB");
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
