package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import net.minecraft.src.EntityPlayerMP;

public interface IWailaPacket {

    void encode(DataOutputStream target) throws Exception;

    void decode(DataInputStream dat);

    void handleServer(EntityPlayerMP sender);

}
