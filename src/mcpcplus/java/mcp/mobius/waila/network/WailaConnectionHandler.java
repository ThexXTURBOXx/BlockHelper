package mcp.mobius.waila.network;

import forge.IConnectionHandler;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.NetHandler;
import net.minecraft.server.NetServerHandler;
import net.minecraft.server.NetworkManager;
import net.minecraft.server.Packet1Login;

public class WailaConnectionHandler implements IConnectionHandler {

    @Override
    public void onConnect(NetworkManager network) {
    }

    @Override
    public void onLogin(NetworkManager network, Packet1Login login) {
        NetHandler handler = network.getNetHandler();
        if (handler instanceof NetServerHandler) {
            EntityPlayer player = ((NetServerHandler) handler).getPlayerEntity();
            WailaPacketHandler.sendPacketToPlayer(new Packet0x00ServerPing(), player);
        }
    }

    @Override
    public void onDisconnect(NetworkManager network, String message, Object[] args) {
    }

}
