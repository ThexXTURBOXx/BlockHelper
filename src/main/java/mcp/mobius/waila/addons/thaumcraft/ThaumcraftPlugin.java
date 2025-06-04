package mcp.mobius.waila.addons.thaumcraft;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class ThaumcraftPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ThaumcraftPlugin();

    static Class<?> TileCrystalCapacitor;
    static Field TileCrystalCapacitor_maxVis;

    private ThaumcraftPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("thaumcraft.common.Thaumcraft");
            mod_BlockHelper.LOG.log(Level.INFO, "[Thaumcraft] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Thaumcraft] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            TileCrystalCapacitor = AccessHelper.getClass("thaumcraft.common.blocks.TileCrystalCapacitor");
            TileCrystalCapacitor_maxVis = AccessHelper.getField(TileCrystalCapacitor, "maxVis");

            registrar.addSyncedConfig("Thaumcraft", "thaumcraft.storedvis");

            registrar.registerNBTProvider(HUDHandlerVis.INSTANCE, TileCrystalCapacitor);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerVis.INSTANCE, TileCrystalCapacitor);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thaumcraft] Error while loading crystal capacitor hooks.", t);
        }
    }

}
