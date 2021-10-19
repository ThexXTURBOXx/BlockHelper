package de.thexxturboxx.blockhelper.api;

import net.minecraft.src.Block;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class BlockHelperBlockState {

    public final World world;
    public final MovingObjectPosition mop;
    public final Block block;
    public final TileEntity te;
    public final int id;
    public final int meta;

    public BlockHelperBlockState(World world, MovingObjectPosition mop, Block block, TileEntity te, int id, int meta) {
        this.world = world;
        this.mop = mop;
        this.block = block;
        this.te = te;
        this.id = id;
        this.meta = meta;
    }

}
