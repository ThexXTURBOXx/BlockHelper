package de.thexxturboxx.blockhelper.api;

import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockHelperBlockState {

    public final World world;
    public final Block block;
    public final TileEntity te;
    public final int id;
    public final int meta;

    public BlockHelperBlockState(World world, Block block, TileEntity te, int id, int meta) {
        this.world = world;
        this.block = block;
        this.te = te;
        this.id = id;
        this.meta = meta;
    }

}
