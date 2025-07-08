package mcp.mobius.waila.api.event;

import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.World;

public class ClientFirstTickInWorldEvent {

    private final Minecraft minecraft;
    private final World world;
    private final EntityPlayerSP player;

    public ClientFirstTickInWorldEvent(Minecraft minecraft) {
        this.minecraft = minecraft;
        this.world = minecraft.theWorld;
        this.player = minecraft.thePlayer;
    }

    public Minecraft getMinecraft() {
        return minecraft;
    }

    public World getWorld() {
        return world;
    }

    public EntityPlayerSP getPlayer() {
        return player;
    }

}
