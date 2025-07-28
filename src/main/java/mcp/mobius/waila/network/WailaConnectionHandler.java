package mcp.mobius.waila.network;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import java.lang.reflect.Field;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.NetLoginHandler;
import net.minecraft.network.packet.NetHandler;
import net.minecraft.network.packet.Packet1Login;
import net.minecraft.server.MinecraftServer;

public class WailaConnectionHandler implements IConnectionHandler {

    private static final Field disconnected;

    static {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            try {
                disconnected = AccessHelper.getDeclaredField(NetClientHandler.class,
                        "f", "field_72554_f", "disconnected");
            } catch (Throwable t) {
                throw new RuntimeException(t);
            }
        } else {
            disconnected = null;
        }
    }

    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, INetworkManager manager) {
        WailaPacketHandler.sendPacketToPlayer(new Packet0x00ServerPing(), player);
    }

    @Override
    public String connectionReceived(NetLoginHandler netHandler, INetworkManager manager) {
        return null;
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, INetworkManager manager) {
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, INetworkManager manager) {
    }

    @Override
    public void connectionClosed(INetworkManager manager) {
        try {
            if (disconnected != null && disconnected.getBoolean(Minecraft.getMinecraft().thePlayer.sendQueue))
                Packet0x00ServerPing.resetClient();
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clientLoggedIn(NetHandler clientHandler, INetworkManager manager, Packet1Login login) {
    }

}
