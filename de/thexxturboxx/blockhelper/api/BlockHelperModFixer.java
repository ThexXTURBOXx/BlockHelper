package de.thexxturboxx.blockhelper.api;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public interface BlockHelperModFixer {

    String getMod(Block block, TileEntity te, int id, int meta);

}
