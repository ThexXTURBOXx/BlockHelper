package mcp.mobius.waila.addons.totalpanels;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public class TotalPanelsPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new TotalPanelsPlugin();

    public static Class<?> TileEntityPanel;
    public static Field TileEntityPanel_storage;
    public static Field TileEntityPanel_maxStorage;

    public static Class<?> TileEntityHighPanel;
    public static Field TileEntityHighPanel_storage;
    public static Field TileEntityHighPanel_maxStorage;

    private TotalPanelsPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("com.harley.totalpanels.common.TotalPanels");
            mod_BlockHelper.LOG.log(Level.INFO, "[Total Solar Panels] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Total Solar Panels] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            TileEntityPanel = AccessHelper.getClass("com.harley.totalpanels.common.tile.TileEntityPanel");
            TileEntityPanel_storage = AccessHelper.getField(TileEntityPanel, "storage");
            TileEntityPanel_maxStorage = AccessHelper.getField(TileEntityPanel, "maxStorage");

            TileEntityHighPanel = AccessHelper.getClass("com.harley.totalpanels.common.tile.TileEntityHighPanel");
            TileEntityHighPanel_storage = AccessHelper.getField(TileEntityHighPanel, "storage");
            TileEntityHighPanel_maxStorage = AccessHelper.getField(TileEntityHighPanel, "maxStorage");

            registrar.addSyncedConfig("Total Solar Panels", "totalsolars.storage");

            if (side.isClient())
                registrar.addConfig("Total Solar Panels", "totalsolars.energybars");

            registrar.registerNBTProvider(HUDHandlerTotalSolars.INSTANCE, TileEntityPanel);
            registrar.registerNBTProvider(HUDHandlerTotalSolars.INSTANCE, TileEntityHighPanel);

            if (side.isClient()) {
                registrar.registerBodyProvider(HUDHandlerTotalSolars.INSTANCE, TileEntityPanel);
                registrar.registerBodyProvider(HUDHandlerTotalSolars.INSTANCE, TileEntityHighPanel);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Total Solar Panels] Error while loading generator hooks.", t);
        }
    }

}
