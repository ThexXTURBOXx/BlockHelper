package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import net.minecraft.src.EntityPlayerSP;

public interface IWailaPacket {

    void encode(DataOutputStream target) throws Exception;

    void decode(DataInputStream dat);

    void handleClient();

    void handleServer(EntityPlayerSP sender);

}
