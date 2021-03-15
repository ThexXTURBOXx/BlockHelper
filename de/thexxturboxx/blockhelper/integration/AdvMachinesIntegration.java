package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import mods.immibis.am2.TileAM2Base;
import net.minecraft.tileentity.TileEntity;

public class AdvMachinesIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "mods.immibis.am2.TileAM2Base")) {
            TileAM2Base tam = (TileAM2Base) te;
            info.add(this.<Integer>getDeclaredField(tam, "storedEnergy") + " EU / "
                    + this.<Integer>getDeclaredField(TileAM2Base.class, "MAX_STORAGE") + " EU");
        }
    }

}
