package mcp.mobius.waila.addons.thermalexpansion;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
    public static Method TileTank_getTankFluid = null;
    public static Method TileTank_getTankCapacity = null;
    public static Method TileTank_getTankAmount = null;
    public static Field TileTank_mode = null;

    public static Class<?> TileTesseract = null;
    public static Field TileTesseract_Item = null;
    public static Field TileTesseract_Fluid = null;
    public static Field TileTesseract_Energy = null;

    private ThermalExpansionPlugin() {
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        // XXX : We register the energy cell
        try {
            TileEnergyCell = Class.forName("cofh.thermalexpansion.block.cell.TileCell");
            TileEnergyCell_Recv = TileEnergyCell.getDeclaredField("energyReceive");
            TileEnergyCell_Send = TileEnergyCell.getDeclaredField("energySend");

            registrar.addConfigRemote("Thermal Expansion", "thermalexpansion.energycell");

            registrar.registerBodyProvider(HUDHandlerEnergyCell.INSTANCE, TileEnergyCell);
            registrar.registerNBTProvider(HUDHandlerEnergyCell.INSTANCE, TileEnergyCell);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Energy Cell hooks.", t);
        }

        // XXX : We register the Tank interface
        try {
            TileTank = Class.forName("cofh.thermalexpansion.block.tank.TileTank");
            TileTank_getTankFluid = TileTank.getMethod("getTankFluid");
            TileTank_getTankCapacity = TileTank.getMethod("getTankCapacity");
            TileTank_getTankAmount = TileTank.getMethod("getTankFluidAmount");
            TileTank_mode = TileTank.getField("mode");

            registrar.addConfigRemote("Thermal Expansion", "thermalexpansion.fluidtype");
            registrar.addConfigRemote("Thermal Expansion", "thermalexpansion.fluidamount");
            registrar.addConfig("Thermal Expansion", "thermalexpansion.tankmode");

            registrar.registerHeadProvider(HUDHandlerTank.INSTANCE, TileTank);
            registrar.registerBodyProvider(HUDHandlerTank.INSTANCE, TileTank);
            registrar.registerNBTProvider(HUDHandlerTank.INSTANCE, TileTank);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Tank hooks.", t);
        }

        // XXX : We register the Tesseract interface
        try {
            TileTesseract = Class.forName("cofh.thermalexpansion.block.ender.TileTesseract");
            TileTesseract_Item = TileTesseract.getDeclaredField("modeItem");
            TileTesseract_Fluid = TileTesseract.getDeclaredField("modeFluid");
            TileTesseract_Energy = TileTesseract.getDeclaredField("modeEnergy");

            registrar.addConfigRemote("Thermal Expansion", "thermalexpansion.tesssendrecv");
            registrar.addConfigRemote("Thermal Expansion", "thermalexpansion.tessfreq");

            registrar.registerBodyProvider(HUDHandlerTesseract.INSTANCE, TileTesseract);
            registrar.registerNBTProvider(HUDHandlerTesseract.INSTANCE, TileTesseract);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Tesseract hooks.", t);
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
    }

}
