package mcp.mobius.waila.addons.thermalexpansion;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public final class ThermalExpansionPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ThermalExpansionPlugin();

    public static Class<?> TileEnergyCell = null;
    public static Field TileEnergyCell_Recv = null;
    public static Field TileEnergyCell_Send = null;

    public static Class<?> TileTank = null;
    public static Field TileTank_mode = null;

    public static Class<?> TileTesseractRoot = null;
    public static Class<?> TileTesseractItem = null;
    public static Class<?> TileTesseractLiquid = null;
    public static Class<?> TileTesseractEnergy = null;

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
    public void register(IRegistrar registrar, Side side) {
        // XXX: We register the energy cell
        try {
            TileEnergyCell = AccessHelper.getClass("thermalexpansion.block.device.TileEnergyCell");
            TileEnergyCell_Recv = AccessHelper.getField(TileEnergyCell, "energyReceive");
            TileEnergyCell_Send = AccessHelper.getField(TileEnergyCell, "energySend");

            registrar.addSyncedConfig("Thermal Expansion", "thermalexpansion.energycell");

            registrar.registerNBTProvider(HUDHandlerEnergyCell.INSTANCE, TileEnergyCell);

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerEnergyCell.INSTANCE, TileEnergyCell);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Energy Cell hooks.", t);
        }

        // XXX: We register the Tesseract interface
        try {
            TileTesseractRoot = AccessHelper.getClass("thermalexpansion.block.tesseract.TileTesseractRoot");
            TileTesseractItem = AccessHelper.getClass("thermalexpansion.block.tesseract.TileTesseractItem");
            TileTesseractLiquid = AccessHelper.getClass("thermalexpansion.block.tesseract.TileTesseractLiquid");
            TileTesseractEnergy = AccessHelper.getClass("thermalexpansion.block.tesseract.TileTesseractEnergy");

            registrar.addSyncedConfig("Thermal Expansion", "thermalexpansion.tesssendrecv");
            registrar.addSyncedConfig("Thermal Expansion", "thermalexpansion.tessfreq");

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerTesseract.INSTANCE, TileTesseractRoot);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Tesseract hooks.", t);
        }

        if (side.isClient()) {
            // XXX: We register the Tank interface
            try {
                TileTank = AccessHelper.getClass("thermalexpansion.block.device.TileTankPortable");
                TileTank_mode = AccessHelper.getField(TileTank, "mode");

                registrar.addConfig("Thermal Expansion", "thermalexpansion.tankmode");
                registrar.registerBodyProvider(HUDHandlerTank.INSTANCE, TileTank);
            } catch (Throwable t) {
                mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Tank hooks.", t);
            }
        }
    }

}
