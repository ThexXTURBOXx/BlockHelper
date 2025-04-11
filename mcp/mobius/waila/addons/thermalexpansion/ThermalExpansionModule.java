package mcp.mobius.waila.addons.thermalexpansion;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraftforge.common.ForgeDirection;

public class ThermalExpansionModule {

    public static Class<?> IEnergyProvider = null;
    public static Method IEnergyProvider_getMaxStorage = null;
    public static Method IEnergyProvider_getCurStorage = null;

    public static Class<?> IEnergyReceiver = null;
    public static Method IEnergyReceiver_getMaxStorage = null;
    public static Method IEnergyReceiver_getCurStorage = null;

    public static Class<?> IEnergyInfo = null;
    public static Method IEnergyInfo_getMaxStorage = null;
    public static Method IEnergyInfo_getCurStorage = null;

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

    public static Class<?> TileCache = null;
    public static Method TileCache_getItemStack = null;
    public static Method TileCache_getMaxStored = null;
    public static Method TileCache_getStored = null;

    public static void register() {
        // XXX : We register the Energy interface first
        try {
            IEnergyProvider = Class.forName("cofh.api.energy.IEnergyProvider");
            IEnergyProvider_getMaxStorage = IEnergyProvider.getMethod("getMaxEnergyStored", ForgeDirection.class);
            IEnergyProvider_getCurStorage = IEnergyProvider.getMethod("getEnergyStored", ForgeDirection.class);

            IEnergyReceiver = Class.forName("cofh.api.energy.IEnergyReceiver");
            IEnergyReceiver_getMaxStorage = IEnergyReceiver.getMethod("getMaxEnergyStored", ForgeDirection.class);
            IEnergyReceiver_getCurStorage = IEnergyReceiver.getMethod("getEnergyStored", ForgeDirection.class);

            IEnergyInfo = Class.forName("cofh.api.tileentity.IEnergyInfo");
            IEnergyInfo_getMaxStorage = IEnergyInfo.getMethod("getInfoMaxEnergyStored");
            IEnergyInfo_getCurStorage = IEnergyInfo.getMethod("getInfoEnergyStored");


            WailaRegistrar.instance().addConfigRemote("Thermal Expansion", "thermalexpansion.energyhandler");
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerIEnergyHandler(), IEnergyProvider);
            WailaRegistrar.instance().registerNBTProvider(new HUDHandlerIEnergyHandler(), IEnergyProvider);
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerIEnergyHandler(), IEnergyReceiver);
            WailaRegistrar.instance().registerNBTProvider(new HUDHandlerIEnergyHandler(), IEnergyReceiver);
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerIEnergyHandler(), IEnergyInfo);
            WailaRegistrar.instance().registerNBTProvider(new HUDHandlerIEnergyHandler(), IEnergyInfo);

        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Energy hooks.", t);
        }

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

        // XXX : We register the Cache interface
        try {
            TileCache = Class.forName("cofh.thermalexpansion.block.cache.TileCache");
            TileCache_getItemStack = TileCache.getDeclaredMethod("getStoredItemType");
            TileCache_getMaxStored = TileCache.getDeclaredMethod("getMaxStoredCount");
            TileCache_getStored = TileCache.getDeclaredMethod("getStoredCount");

            WailaRegistrar.instance().addConfigRemote("Thermal Expansion", "thermalexpansion.cache");
            WailaRegistrar.instance().registerHeadProvider(new HUDHandlerCache(), TileCache);
            WailaRegistrar.instance().registerBodyProvider(new HUDHandlerCache(), TileCache);
            WailaRegistrar.instance().registerNBTProvider(new HUDHandlerCache(), TileCache);

        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Thermal Expansion] Error while loading Tesseract hooks.", t);
        }
    }


}
