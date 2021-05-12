package de.thexxturboxx.blockhelper.api;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHelperState {

    public final World world;
    public final Block block;
    public final TileEntity te;
    public final int id;
    public final int meta;

    public BlockHelperState(World world, Block block, TileEntity te, int id, int meta) {
        this.world = world;
        this.block = block;
        this.te = te;
        this.id = id;
        this.meta = meta;
    }

}
