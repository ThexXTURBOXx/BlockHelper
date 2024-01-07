package de.thexxturboxx.blockhelper.api;

import net.minecraft.server.Block;
import net.minecraft.server.MovingObjectPosition;
import net.minecraft.server.StatisticStorage;
import net.minecraft.server.TileEntity;
import net.minecraft.server.World;

/**
 * Contains information about a block to inspect.
 */
public class BlockHelperBlockState {

    /**
     * The translator for the correct language.
     */
    public final StatisticStorage translator;

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
     * @param translator The translator for the correct language.
     * @param world      The current {@link World} the block is in.
     * @param mop        The current raytrace result as a {@link MovingObjectPosition}.
     * @param block      The current block as a {@link Block} object.
     * @param te         The current tile entity as a {@link TileEntity} object. Can be {@code null}!
     * @param id         The current ID of the block to inspect.
     * @param meta       The current metadata of the block to inspect.
     */
    public BlockHelperBlockState(StatisticStorage translator, World world, MovingObjectPosition mop, Block block,
                                 TileEntity te, int id, int meta) {
        this.translator = translator;
        this.world = world;
        this.mop = mop;
        this.block = block;
        this.te = te;
        this.id = id;
        this.meta = meta;
    }

}
