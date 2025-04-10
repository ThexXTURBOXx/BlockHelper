package mcp.mobius.waila.addons.agriculture;

import java.util.logging.Level;

import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.api.impl.ModuleRegistrar;

public class AgricultureModule {
	public static Class BlockCrop  = null;


	public static void register(){
		try{
			Class Agriculture = Class.forName("com.teammetallurgy.agriculture.Agriculture");
			mod_BlockHelper.log.log(Level.INFO, "Agriculture mod found.");
		} catch (ClassNotFoundException e){
			mod_BlockHelper.log.log(Level.INFO, "[Agriculture] Agriculture mod not found.");
			return;
		}

		try{
			BlockCrop = Class.forName("com.teammetallurgy.agriculture.crops.BlockCrop");
		} catch (ClassNotFoundException e){
			mod_BlockHelper.log.log(Level.WARNING, "[Agriculture] Class not found. " + e);
			return;
		}

		ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerCrop(),  BlockCrop);
	}
}
