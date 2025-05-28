package mcp.mobius.waila.api;

import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.World;

/**
 * The Accessor is used to get some basic data out of the game without having to request direct access to
 * the game engine.
 */
public interface IServerCommonAccessor {

    World getWorld();

    EntityPlayerMP getPlayer();

}
