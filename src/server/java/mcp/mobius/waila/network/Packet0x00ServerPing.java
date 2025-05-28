package mcp.mobius.waila.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;
import java.util.Map;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import mcp.mobius.waila.utils.config.Property;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.Packet;

public class Packet0x00ServerPing implements IWailaPacket {

    Map<String, Boolean> forcedKeys = new HashMap<String, Boolean>();

    public Packet0x00ServerPing() {
        Map<String, Property> serverForcingCfg =
                PluginConfig.instance().config.categories.get(Constants.CATEGORY_SERVER);

        for (String key : serverForcingCfg.keySet())
            if (serverForcingCfg.get(key).getBoolean(false))
                forcedKeys.put(key, PluginConfig.instance().get(key));
    }

    @Override
    public void encode(DataOutputStream target) throws Exception {
        target.writeShort(this.forcedKeys.size());
        for (String key : forcedKeys.keySet()) {
            Packet.func_27038_a(key, target);
            target.writeBoolean(this.forcedKeys.get(key));
        }
    }

    @Override
    public void decode(DataInputStream dat) {
        try {
            this.forcedKeys = new HashMap<String, Boolean>();
            int nkeys = dat.readShort();
            for (int i = 0; i < nkeys; i++)
                this.forcedKeys.put(Packet.func_27037_a(dat, 255), dat.readBoolean());
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, this.getClass());
        }

    }

    @Override
    public void handleServer(EntityPlayerMP sender) {
    }

}
