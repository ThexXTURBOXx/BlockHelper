package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ConfigCategory;

public class Packet0x00ServerPing implements IWailaPacket {

    Map<String, Boolean> forcedKeys = new HashMap<String, Boolean>();

    public Packet0x00ServerPing() {
        ConfigCategory serverForcingCfg = PluginConfig.instance().config.getCategory(Constants.CATEGORY_SERVER);

        // Use entrySet() instead of keySet() to avoid double lookup
        for (Map.Entry<String, ?> entry : serverForcingCfg.entrySet())
            if (serverForcingCfg.get(entry.getKey()).getBoolean(false))
                forcedKeys.put(entry.getKey(), PluginConfig.instance().get(entry.getKey()));
    }

    @Override
    public void encode(DataOutputStream target) throws Exception {
        target.writeShort(this.forcedKeys.size());
        // Use entrySet() instead of keySet() to avoid double lookup
        for (Map.Entry<String, Boolean> entry : forcedKeys.entrySet()) {
            Packet.writeString(entry.getKey(), target);
            target.writeBoolean(entry.getValue());
        }
    }

    @Override
    public void decode(DataInputStream dat) {
        try {
            this.forcedKeys = new HashMap<String, Boolean>();
            int nkeys = dat.readShort();
            for (int i = 0; i < nkeys; i++)
                this.forcedKeys.put(Packet.readString(dat, 255), dat.readBoolean());
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
    public void handleServer(Player rawSender) {
    }

    public static void resetClient() {
        mod_BlockHelper.INSTANCE.serverPresent = false;
        PluginConfig.instance().forcedConfigs = new HashMap<String, Boolean>();
    }

}
