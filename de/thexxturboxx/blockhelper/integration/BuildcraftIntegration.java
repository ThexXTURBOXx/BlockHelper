package de.thexxturboxx.blockhelper.integration;

import buildcraft.energy.TileEngine;
import buildcraft.factory.TileMachine;
import buildcraft.factory.TileTank;
import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import java.util.Map;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.liquids.LiquidDictionary;
import net.minecraftforge.liquids.LiquidStack;

public class BuildcraftIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "buildcraft.energy.TileEngine")) {
            if (((TileEngine) te).engine != null) {
                info.add(1, ((TileEngine) te).engine.getEnergyStored() + " MJ / "
                        + ((TileEngine) te).engine.maxEnergy + " MJ");
            }
        } else if (iof(te, "buildcraft.factory.TileMachine")) {
            info.add(1, ((TileMachine) te).getPowerProvider().getEnergyStored() + " MJ / "
                    + ((TileMachine) te).getPowerProvider().getMaxEnergyStored() + " MJ");
        } else if (iof(te, "buildcraft.factory.TileTank")) {
            if (((TileTank) te).tank.getLiquid() != null) {
                String name = getLiquidName(((TileTank) te).tank.getLiquid().asItemStack().itemID);
                if (name.equals("")) {
                    info.add(1, "0 mB / " + ((TileTank) te).tank.getCapacity() + " mB");
                } else {
                    info.add(1, ((TileTank) te).tank.getLiquid().amount + " mB / "
                            + ((TileTank) te).tank.getCapacity() + " mB of " + name);
                }
            } else {
                info.add(1, "0 mB / " + ((TileTank) te).tank.getCapacity() + " mB");
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
