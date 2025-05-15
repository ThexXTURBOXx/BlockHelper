package mcp.mobius.waila.api;

import net.minecraft.src.Entity;

/**
 * The Accessor is used to get some basic data out of the game without having to request direct access to
 * the game engine.
 */
public interface IServerEntityAccessor extends IServerCommonAccessor {

    Entity getEntity();

}
