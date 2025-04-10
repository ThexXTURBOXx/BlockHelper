package mcp.mobius.waila.network;

import cpw.mods.fml.common.network.Player;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.HashMap;

import mcp.mobius.waila.mod_Waila;
import net.minecraft.network.packet.Packet;
import net.minecraftforge.common.ConfigCategory;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.WailaExceptionHandler;

public class Packet0x00ServerPing implements IWailaPacket {

	HashMap<String, Boolean> forcedKeys = new HashMap<String, Boolean>();

	public Packet0x00ServerPing(){
		ConfigCategory serverForcingCfg = ConfigHandler.instance().config.getCategory(Constants.CATEGORY_SERVER);

		for (String key : serverForcingCfg.keySet()){
			if (serverForcingCfg.get(key).getBoolean(false)){
				forcedKeys.put(key, ConfigHandler.instance().getConfig(key));
			}
		}
	}

	@Override
	public void encode(DataOutputStream target) throws Exception {
		target.writeShort(this.forcedKeys.size());
		for (String key : forcedKeys.keySet()){
			Packet.writeString(key, target);
			target.writeBoolean(this.forcedKeys.get(key));
		}
	}

	@Override
	public void decode(DataInputStream dat) {
		try{
			int nkeys = dat.readShort();
			for (int i = 0; i < nkeys; i++){
				this.forcedKeys.put(Packet.readString(dat, 255), dat.readBoolean());
			}
		}catch (Exception e){
    		WailaExceptionHandler.handleErr(e, this.getClass().toString(), null);
		}

	}

	@Override
	public void handle(Player player) {
		mod_Waila.log.info("Received server authentication packet. Remote sync will be activated");
		mod_Waila.instance.serverPresent = true;

		for (String key : forcedKeys.keySet())
			mod_Waila.log.info(String.format("Received forced key config %s : %s", key, forcedKeys.get(key)));

		ConfigHandler.instance().forcedConfigs = forcedKeys;
	}

}
