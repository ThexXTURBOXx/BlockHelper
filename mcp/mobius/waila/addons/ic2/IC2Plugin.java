package mcp.mobius.waila.addons.ic2;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

public final class IC2Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new IC2Plugin();

    public static Class<?> TileBaseGenerator = null;
    public static Field TileBaseGenerator_storage = null;
    public static Field TileBaseGenerator_maxStorage = null;
    public static Field TileBaseGenerator_production = null;

    private IC2Plugin() {
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        try {
            Class.forName("ic2.core.IC2");
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft 2] Mod found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft 2] Mod not found.");
            return;
        }

        // XXX : We register the Energy interface first
        try {
            TileBaseGenerator = Class.forName("ic2.core.block.generator.tileentity.TileEntityBaseGenerator");
            TileBaseGenerator_storage = TileBaseGenerator.getField("storage");
            TileBaseGenerator_maxStorage = TileBaseGenerator.getField("maxStorage");
            TileBaseGenerator_production = TileBaseGenerator.getField("production");

            registrar.addSyncedConfig("IndustrialCraft2", "ic2.storage");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.outputeu");

            registrar.registerBodyProvider(HUDHandlerIC2Generator.INSTANCE, TileBaseGenerator);
            registrar.registerNBTProvider(HUDHandlerIC2Generator.INSTANCE, TileBaseGenerator);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading generator hooks.", t);
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
