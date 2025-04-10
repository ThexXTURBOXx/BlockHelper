package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;

public interface IWailaPacket {
    void encode(DataOutputStream target) throws Exception;

    void decode(DataInputStream dat);

    void handle(Player player);
}
