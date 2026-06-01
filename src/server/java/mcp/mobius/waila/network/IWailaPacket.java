package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import net.minecraft.src.EntityPlayerMP;

public interface IWailaPacket {

    int MAX_REACH_SQ = 36;

    void encode(DataOutputStream target) throws Exception;

    void decode(DataInputStream dat);

    void handleServer(EntityPlayerMP sender);

}
