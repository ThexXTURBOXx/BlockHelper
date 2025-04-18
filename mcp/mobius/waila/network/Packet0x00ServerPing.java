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

        for (String key : serverForcingCfg.keySet())
            if (serverForcingCfg.get(key).getBoolean(false))
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
            int nkeys = dat.readShort();
            for (int i = 0; i < nkeys; i++)
                this.forcedKeys.put(Packet.readString(dat, 255), dat.readBoolean());
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, this.getClass().toString(), null);
        }

    }

    @Override
    public void handleClient() {
        mod_BlockHelper.LOG.info("Received server authentication packet. Remote sync will be activated");
        mod_BlockHelper.INSTANCE.serverPresent = true;

        for (String key : forcedKeys.keySet())
            mod_BlockHelper.LOG.info("Received forced key config " + key + " : " + forcedKeys.get(key));

        PluginConfig.instance().forcedConfigs = forcedKeys;
    }

    @Override
    public void handleServer(Player rawSender) {
    }

}
