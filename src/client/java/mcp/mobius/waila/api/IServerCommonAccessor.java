package mcp.mobius.waila.api;

import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.World;

/**
 * The Accessor is used to get some basic data out of the game without having to request direct access to
 * the game engine.
 */
public interface IServerCommonAccessor {

    World getWorld();

    EntityPlayerSP getPlayer();

}
