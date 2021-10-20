package de.thexxturboxx.blockhelper.api;

import net.minecraft.server.Entity;
import net.minecraft.server.World;

/**
 * Contains information about an entity to inspect.
 */
public class BlockHelperEntityState {

    /**
     * The current {@link World} the entity is in.
     */
    public final World world;

    /**
     * The current {@link Entity} to inspect.
     */
    public final Entity entity;

    /**
     * Constructs a new {@link BlockHelperEntityState} containing information about an entity to inspect.
     *
     * @param world  The current {@link World} the entity is in.
     * @param entity The current {@link Entity} to inspect.
     */
    public BlockHelperEntityState(World world, Entity entity) {
        this.world = world;
        this.entity = entity;
    }

}
