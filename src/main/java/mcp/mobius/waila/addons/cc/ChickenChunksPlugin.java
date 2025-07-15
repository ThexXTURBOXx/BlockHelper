package mcp.mobius.waila.addons.cc;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public final class ChickenChunksPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ChickenChunksPlugin();

    public static Field TileChunkLoaderBase_active = null;

    public static Class<?> TileChunkLoader = null;
    public static Field TileChunkLoader_shape = null;

    private ChickenChunksPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("codechicken.chunkloader.ChickenChunks");
            mod_BlockHelper.LOG.log(Level.INFO, "[ChickenChunks] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[ChickenChunks] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            Class<?> TileChunkLoaderBase = AccessHelper.getClass("codechicken.chunkloader.TileChunkLoaderBase");
            TileChunkLoaderBase_active = AccessHelper.getField(TileChunkLoaderBase, "active");

            TileChunkLoader = AccessHelper.getClass("codechicken.chunkloader.TileChunkLoader");
            TileChunkLoader_shape = AccessHelper.getField(TileChunkLoader, "shape");

            registrar.addSyncedConfig("ChickenChunks", "cc.owner");
            registrar.addSyncedConfig("ChickenChunks", "cc.active");
            registrar.addSyncedConfig("ChickenChunks", "cc.radius");
            registrar.addSyncedConfig("ChickenChunks", "cc.shape");

            registrar.registerNBTProvider(HUDHandlerChunkLoader.INSTANCE, TileChunkLoaderBase);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerChunkLoader.INSTANCE, TileChunkLoaderBase);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[ChickenChunks] Error while loading chunk loader hooks.", t);
        }
    }

}
