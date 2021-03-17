package de.thexxturboxx.blockhelper.api;

import net.minecraft.server.Block;
import net.minecraft.server.TileEntity;

public interface BlockHelperNameFixer {

    String getName(Block block, TileEntity te, int id, int meta);

}
