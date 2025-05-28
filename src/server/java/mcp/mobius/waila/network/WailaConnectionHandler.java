package mcp.mobius.waila.network;

import forge.IConnectionHandler;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.NetHandler;
import net.minecraft.src.NetServerHandler;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet1Login;

public class WailaConnectionHandler implements IConnectionHandler {

    @Override
    public void onConnect(NetworkManager network) {
    }

    @Override
    public void onLogin(NetworkManager network, Packet1Login login) {
        NetHandler handler = network.getNetHandler();
        if (handler instanceof NetServerHandler) {
            EntityPlayerMP player = ((NetServerHandler) handler).getPlayerEntity();
            WailaPacketHandler.sendPacketToPlayer(new Packet0x00ServerPing(), player);
        }
    }

    @Override
    public void onDisconnect(NetworkManager network, String message, Object[] args) {
    }

}
