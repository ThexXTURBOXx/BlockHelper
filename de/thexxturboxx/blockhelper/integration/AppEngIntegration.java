package de.thexxturboxx.blockhelper.integration;

import appeng.me.basetiles.TilePoweredBase;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import net.minecraft.tileentity.TileEntity;

public class AppEngIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "appeng.me.basetiles.TilePoweredBase")) {
            info.add(((TilePoweredBase) te).storedPower + " AE / "
                    + ((TilePoweredBase) te).maxStoredPower + " AE");
        }
    }

}
