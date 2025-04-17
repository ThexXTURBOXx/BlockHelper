package mcp.mobius.waila.addons.thermalexpansion;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;

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
    public void registerCommon(IRegistrar registrar) {
        try {
            Class.forName("thermalexpansion.ThermalExpansion");
            mod_BlockHelper.LOG.log(Level.INFO, "[Thermal Expansion] Mod found.");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Thermal Expansion] Mod not found.");
            return;
        }

        // XXX : We register the energy cell
        try {
            TileEnergyCell = Class.forName("thermalexpansion.block.device.TileEnergyCell");
            TileEnergyCell_Recv = TileEnergyCell.getField("energyReceive");
            TileEnergyCell_Send = TileEnergyCell.getField("energySend");

            registrar.addSyncedConfig("Thermal Expansion", "thermalexpansion.energycell");

            registrar.registerBodyProvider(HUDHandlerEnergyCell.INSTANCE, TileEnergyCell);
            registrar.registerNBTProvider(HUDHandlerEnergyCell.INSTANCE, TileEnergyCell);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Energy Cell hooks.", t);
        }

        // XXX : We register the Tank interface
        try {
            TileTank = Class.forName("thermalexpansion.block.device.TileTankPortable");
            TileTank_mode = TileTank.getField("mode");

            registrar.addConfig("Thermal Expansion", "thermalexpansion.tankmode");
            registrar.registerBodyProvider(HUDHandlerTank.INSTANCE, TileTank);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Tank hooks.", t);
        }

        // XXX : We register the Tesseract interface
        try {
            TileTesseractRoot = Class.forName("thermalexpansion.block.tesseract.TileTesseractRoot");
            TileTesseractItem = Class.forName("thermalexpansion.block.tesseract.TileTesseractItem");
            TileTesseractLiquid = Class.forName("thermalexpansion.block.tesseract.TileTesseractLiquid");
            TileTesseractEnergy = Class.forName("thermalexpansion.block.tesseract.TileTesseractEnergy");

            registrar.addSyncedConfig("Thermal Expansion", "thermalexpansion.tesssendrecv");
            registrar.addSyncedConfig("Thermal Expansion", "thermalexpansion.tessfreq");

            registrar.registerBodyProvider(HUDHandlerTesseract.INSTANCE, TileTesseractRoot);
            registrar.registerNBTProvider(HUDHandlerTesseract.INSTANCE, TileTesseractRoot);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Tesseract hooks.", t);
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
