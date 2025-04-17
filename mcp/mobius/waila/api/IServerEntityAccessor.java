package mcp.mobius.waila.api;

import net.minecraft.entity.Entity;

public interface IServerEntityAccessor extends IServerCommonAccessor {

    Entity getEntity();

}
