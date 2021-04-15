package de.thexxturboxx.blockhelper.api;

import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

public interface BlockHelperItemStackFixer {

    ItemStack getItemStack(Block block, TileEntity te, int id, int meta);

}
