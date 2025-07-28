package mcp.mobius.waila.addons.thermalexpansion;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class ThermalExpansionPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ThermalExpansionPlugin();

    public static Class<?> TileEnergyCell = null;
    public static Field TileEnergyCell_Recv = null;
    public static Field TileEnergyCell_Send = null;

    private ThermalExpansionPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("thermalexpansion.ThermalExpansion");
            mod_BlockHelper.LOG.log(Level.INFO, "[Thermal Expansion] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Thermal Expansion] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        // XXX: We register the energy cell
        try {
            TileEnergyCell = AccessHelper.getClass("thermalexpansion.energy.tileentity.TileEnergyCell");
            TileEnergyCell_Recv = AccessHelper.getField(TileEnergyCell, "energyReceive");
            TileEnergyCell_Send = AccessHelper.getField(TileEnergyCell, "energySend");

            registrar.addSyncedConfig("Thermal Expansion", "thermalexpansion.energycell");

            registrar.registerNBTProvider(HUDHandlerEnergyCell.INSTANCE, TileEnergyCell);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Energy Cell hooks.", t);
        }
    }

}
