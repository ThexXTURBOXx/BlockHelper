package de.thexxturboxx.blockhelper.api;

import net.minecraft.server.Entity;
import net.minecraft.server.World;

public class BlockHelperEntityState {

    public final World world;
    public final Entity entity;

    public BlockHelperEntityState(World world, Entity entity) {
        this.world = world;
        this.entity = entity;
    }

}
