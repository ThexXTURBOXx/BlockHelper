package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface IWailaPacket {

    int MAX_REACH_SQ = 36;

    void encode(DataOutputStream target) throws Exception;

    void decode(DataInputStream dat);

    void handleClient();

    void handleServer(Player rawSender);

}
