package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.Packet;
import net.minecraft.src.mod_BlockHelper;

public class Packet0x00ServerPing implements IWailaPacket {

    Map<String, Boolean> forcedKeys = new HashMap<String, Boolean>();

    public Packet0x00ServerPing() {
    }

    @Override
    public void encode(DataOutputStream target) throws Exception {
        target.writeShort(this.forcedKeys.size());
        for (String key : forcedKeys.keySet()) {
            Packet.func_27049_a(key, target);
            target.writeBoolean(this.forcedKeys.get(key));
        }
    }

    @Override
    public void decode(DataInputStream dat) {
        try {
            this.forcedKeys = new HashMap<String, Boolean>();
            int nkeys = dat.readShort();
            for (int i = 0; i < nkeys; i++)
                this.forcedKeys.put(Packet.func_27048_a(dat, 255), dat.readBoolean());
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, this.getClass(), null);
        }

    }

    @Override
    public void handleClient() {
        mod_BlockHelper.LOG.info("Received server authentication packet. Remote sync is active: " + forcedKeys.toString());
        mod_BlockHelper.INSTANCE.serverPresent = true;
        PluginConfig.instance().forcedConfigs = forcedKeys;
    }

    @Override
    public void handleServer(EntityPlayerSP sender) {
    }

    public static void resetClient() {
        mod_BlockHelper.INSTANCE.serverPresent = false;
        PluginConfig.instance().forcedConfigs = new HashMap<String, Boolean>();
    }

}
