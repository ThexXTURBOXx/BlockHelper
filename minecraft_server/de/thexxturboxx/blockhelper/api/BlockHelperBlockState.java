package de.thexxturboxx.blockhelper.api;

import net.minecraft.src.Block;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

/**
 * Contains information about a block to inspect.
 */
public class BlockHelperBlockState {

    /**
     * The current {@link World} the block is in.
     */
    public final World world;

    /**
     * The current raytrace result as a {@link MovingObjectPosition}.
     */
    public final MovingObjectPosition mop;

    /**
     * The current block as a {@link Block} object.
     */
    public final Block block;

    /**
     * The current tile entity as a {@link TileEntity} object. Can be {@code null}!
     */
    public final TileEntity te;

    /**
     * The current ID of the block to inspect.
     */
    public final int id;

    /**
     * The current metadata of the block to inspect.
     */
    public final int meta;

    /**
     * Constructs a new {@link BlockHelperBlockState} containing information about a block to inspect.
     *
     * @param world The current {@link World} the block is in.
     * @param mop   The current raytrace result as a {@link MovingObjectPosition}.
     * @param block The current block as a {@link Block} object.
     * @param te    The current tile entity as a {@link TileEntity} object. Can be {@code null}!
     * @param id    The current ID of the block to inspect.
     * @param meta  The current metadata of the block to inspect.
     */
    public BlockHelperBlockState(World world, MovingObjectPosition mop, Block block,
                                 TileEntity te, int id, int meta) {
        this.world = world;
        this.mop = mop;
        this.block = block;
        this.te = te;
        this.id = id;
        this.meta = meta;
    }

}
