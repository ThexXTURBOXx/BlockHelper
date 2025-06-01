package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.IConnectionHandler;
import cpw.mods.fml.common.network.Player;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetLoginHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet1Login;

public class WailaConnectionHandler implements IConnectionHandler {

    @Override
    public void playerLoggedIn(Player player, NetHandler netHandler, NetworkManager manager) {
        WailaPacketHandler.sendPacketToPlayer(new Packet0x00ServerPing(), player);
    }

    @Override
    public String connectionReceived(NetLoginHandler netHandler, NetworkManager manager) {
        return null;
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, String server, int port, NetworkManager manager) {
    }

    @Override
    public void connectionOpened(NetHandler netClientHandler, MinecraftServer server, NetworkManager manager) {
    }

    @Override
    public void connectionClosed(NetworkManager manager) {
        Packet0x00ServerPing.resetClient();
    }

    @Override
    public void clientLoggedIn(NetHandler clientHandler, NetworkManager manager, Packet1Login login) {
    }

}
