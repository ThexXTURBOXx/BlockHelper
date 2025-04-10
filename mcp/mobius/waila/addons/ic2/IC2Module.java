package mcp.mobius.waila.addons.ic2;

import java.lang.reflect.Field;
import java.util.logging.Level;

import mcp.mobius.waila.mod_Waila;
import mcp.mobius.waila.api.impl.ModuleRegistrar;

public class IC2Module {

	public static Class TileBaseGenerator = null;
	public static Field TileBaseGenerator_storage    = null;
	public static Field TileBaseGenerator_maxStorage = null;
	public static Field TileBaseGenerator_production = null;

	public static void register(){
		// XXX : We register the Energy interface first
		try{
			TileBaseGenerator            = Class.forName("ic2.core.block.generator.tileentity.TileEntityBaseGenerator");
			TileBaseGenerator_storage    = TileBaseGenerator.getField("storage");
			TileBaseGenerator_maxStorage = TileBaseGenerator.getField("maxStorage");
			TileBaseGenerator_production = TileBaseGenerator.getField("production");

			//ModuleRegistrar.instance().addConfigRemote("Thermal Expansion", "thermalexpansion.energyhandler");
			ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerTEGenerator(), TileBaseGenerator);

			ModuleRegistrar.instance().registerNBTProvider (new HUDHandlerTEGenerator(), TileBaseGenerator);

			ModuleRegistrar.instance().addConfigRemote("IndustrialCraft2", "ic2.storage");
			ModuleRegistrar.instance().addConfigRemote("IndustrialCraft2", "ic2.outputeu");

		} catch (Exception e){
			mod_Waila.log.log(Level.WARNING, "[IndustrialCraft 2] Error while loading generator hooks." + e);
		}
	}

}
