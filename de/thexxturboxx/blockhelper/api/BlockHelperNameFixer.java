package de.thexxturboxx.blockhelper.integration;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;

public interface BlockHelperNameFixer {

    String getName(Block block, TileEntity te, int id, int meta);

}
