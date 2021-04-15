package de.thexxturboxx.blockhelper.api;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;

public interface BlockHelperModFixer {

    String getMod(Block block, TileEntity te, int id, int meta);

}
