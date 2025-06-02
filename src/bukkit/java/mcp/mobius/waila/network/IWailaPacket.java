package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import net.minecraft.server.EntityPlayer;

public interface IWailaPacket {

    void encode(DataOutputStream target) throws Exception;

    void decode(DataInputStream dat);

    void handleServer(EntityPlayer sender);

}
