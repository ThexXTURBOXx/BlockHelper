package de.thexxturboxx.blockhelper.api;

import net.minecraft.server.TileEntity;

public interface BlockHelperTileEntityProvider {

    void addInformation(TileEntity te, int id, int meta, InfoHolder info);

}
