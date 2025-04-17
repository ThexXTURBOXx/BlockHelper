package mcp.mobius.waila.api;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.WorldServer;

public interface IServerCommonAccessor {

    WorldServer getWorld();

    EntityPlayerMP getPlayer();

}
