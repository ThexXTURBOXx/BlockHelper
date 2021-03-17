package de.thexxturboxx.blockhelper.api;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public interface BlockHelperNameFixer {

    String getName(Block block, TileEntity te, int id, int meta);

}
