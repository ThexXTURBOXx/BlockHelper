package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Packet;
import net.minecraft.src.mod_BlockHelper;
import net.minecraftforge.common.Property;

public class Packet0x00ServerPing implements IWailaPacket {

    private Map<String, Boolean> forcedKeys = new HashMap<String, Boolean>();

    public Packet0x00ServerPing() {
        Map<String, Property> serverForcingCfg =
                PluginConfig.instance().config.categories.get(Constants.CATEGORY_SERVER);

        for (String key : serverForcingCfg.keySet())
            if (serverForcingCfg.get(key).getBoolean(Constants.SERVER_FREE))
                forcedKeys.put(key, PluginConfig.instance().get(key));
    }

    @Override
    public void encode(DataOutputStream target) throws Exception {
        target.writeShort(this.forcedKeys.size());
        for (String key : forcedKeys.keySet()) {
            Packet.writeString(key, target);
            target.writeBoolean(this.forcedKeys.get(key));
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
