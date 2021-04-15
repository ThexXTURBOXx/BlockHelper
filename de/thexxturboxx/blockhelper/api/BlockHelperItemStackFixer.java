package de.thexxturboxx.blockhelper.api;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public interface BlockHelperItemStackFixer {

    ItemStack getItemStack(Block block, TileEntity te, int id, int meta);

}
