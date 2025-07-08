package mcp.mobius.waila.api.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.event.Event;

public class ClientFirstTickInWorldEvent extends Event {

    private final Minecraft minecraft;
    private final WorldClient world;
    private final EntityClientPlayerMP player;

    public ClientFirstTickInWorldEvent(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.world = minecraft.theWorld;
        this.player = minecraft.thePlayer;
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public WorldClient getWorld() {
        return world;
    }

    public EntityClientPlayerMP getPlayer() {
        return player;
    }

}
