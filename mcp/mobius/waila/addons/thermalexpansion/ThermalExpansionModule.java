package mcp.mobius.waila.addons.thermalexpansion;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraftforge.common.ForgeDirection;

public class ThermalExpansionModule {

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

    public static void register() {
        // XXX : We register the energy cell
        try {
            TileEnergyCell = Class.forName("cofh.thermalexpansion.block.cell.TileCell");
            TileEnergyCell_Recv = TileEnergyCell.getDeclaredField("energyReceive");
            TileEnergyCell_Send = TileEnergyCell.getDeclaredField("energySend");

            WailaRegistrar.instance().addConfigRemote("Thermal Expansion", "thermalexpansion.energycell");
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerEnergyCell(), TileEnergyCell);
            WailaRegistrar.instance().registerNBTProvider(new HUDHandlerEnergyCell(), TileEnergyCell);

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

            WailaRegistrar.instance().addConfigRemote("Thermal Expansion", "thermalexpansion.fluidtype");
            WailaRegistrar.instance().addConfigRemote("Thermal Expansion", "thermalexpansion.fluidamount");
            WailaRegistrar.instance().addConfig("Thermal Expansion", "thermalexpansion.tankmode");
            WailaRegistrar.instance().registerHeadProvider(new HUDHandlerTank(), TileTank);
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerTank(), TileTank);
            WailaRegistrar.instance().registerNBTProvider(new HUDHandlerTank(), TileTank);

        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Tank hooks.", t);
        }

        // XXX : We register the Tesseract interface
        try {
            TileTesseract = Class.forName("cofh.thermalexpansion.block.ender.TileTesseract");
            TileTesseract_Item = TileTesseract.getDeclaredField("modeItem");
            TileTesseract_Fluid = TileTesseract.getDeclaredField("modeFluid");
            TileTesseract_Energy = TileTesseract.getDeclaredField("modeEnergy");

            WailaRegistrar.instance().addConfigRemote("Thermal Expansion", "thermalexpansion.tesssendrecv");
            WailaRegistrar.instance().addConfigRemote("Thermal Expansion", "thermalexpansion.tessfreq");
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerTesseract(), TileTesseract);
            WailaRegistrar.instance().registerNBTProvider(new HUDHandlerTesseract(), TileTesseract);

        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Tesseract hooks.", t);
        }
    }


}
