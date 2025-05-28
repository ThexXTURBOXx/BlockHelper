package mcp.mobius.waila.network;

import forge.IConnectionHandler;
import forge.MessageManager;
import net.minecraft.src.NetworkManager;
import net.minecraft.src.Packet1Login;
import net.minecraft.src.mod_BlockHelper;

public class WailaConnectionHandler implements IConnectionHandler {

    @Override
    public void onConnect(NetworkManager network) {
        MessageManager.getInstance().registerChannel(network, WailaPacketHandler.INSTANCE, mod_BlockHelper.CHANNEL);
    }

    @Override
    public void onLogin(NetworkManager network, Packet1Login login) {
    }

    @Override
    public void onDisconnect(NetworkManager network, String message, Object[] args) {
        Packet0x00ServerPing.resetClient();
    }

}
