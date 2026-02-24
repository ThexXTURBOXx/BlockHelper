package mcp.mobius.waila.addons.bc3;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.BlockCauldron;
import net.minecraft.src.mod_BlockHelper;

public final class BC3Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new BC3Plugin();

    public static Class<?> TileEngine = null;
    public static Field TileEngine_engine = null;

    public static Class<?> Engine = null;
    public static Field Engine_energy = null;
    public static Field Engine_maxEnergy = null;

    public static Class<?> IPowerReceptor = null;
    public static Method IPowerReceptor_getPowerProvider = null;

    public static Class<?> IPowerProvider = null;
    public static Field PowerProvider_energyStored = null;
    public static Field PowerProvider_maxEnergyStored = null;

    public static Class<?> ILiquidContainer = null;
    public static Method ILiquidContainer_getLiquidSlots = null;
    public static Method ILiquidContainer_getLiquidId = null;
    public static Method ILiquidContainer_getLiquidQuantity = null;

    public static Class<?> LiquidSlot = null;
    public static Constructor<?> newLiquidSlot = null;
    public static Method LiquidSlot_getLiquidId = null;
    public static Method LiquidSlot_getLiquidQty = null;
    public static Method LiquidSlot_getCapacity = null;

    public static Class<?> TileGenericPipe = null;
    public static Field TileGenericPipe_pipe = null;

    public static Class<?> Pipe = null;
    public static Field Pipe_itemID = null;

    private BC3Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            Class<?> mod_BuildCraftCore = AccessHelper.getClass("mod_BuildCraftCore");
            String version = (String) AccessHelper.getMethod(mod_BuildCraftCore, new Class[0], "version").invoke(null);
            if (!version.startsWith("3")) throw new Exception("This is not BC3!");
            mod_BlockHelper.LOG.log(Level.INFO, "[BC3] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[BC3] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            TileEngine = AccessHelper.getClass("buildcraft.energy.TileEngine");
            TileEngine_engine = AccessHelper.getField(TileEngine, "engine");

            Engine = AccessHelper.getClass("buildcraft.energy.Engine");
            Engine_energy = AccessHelper.getField(Engine, "energy");
            Engine_maxEnergy = AccessHelper.getField(Engine, "maxEnergy");

            IPowerReceptor = AccessHelper.getClass("buildcraft.api.power.IPowerReceptor",
                    "buildcraft.api.IPowerReceptor");
            IPowerReceptor_getPowerProvider = AccessHelper.getMethod(IPowerReceptor, new Class[0],
                    "getPowerProvider");

            IPowerProvider = AccessHelper.getClass("buildcraft.api.power.IPowerProvider",
                    "buildcraft.api.PowerProvider");
            PowerProvider_energyStored = AccessHelper.getField(IPowerProvider, "energyStored");
            PowerProvider_maxEnergyStored = AccessHelper.getField(IPowerProvider, "maxEnergyStored");

            registrar.addSyncedConfig("Buildcraft", "bcapi.storage");

            registrar.addConfig("Buildcraft", "bcapi.energybars");

            registrar.registerNBTProvider(HUDHandlerBC3Energy.INSTANCE, IPowerReceptor);

            registrar.registerBodyProvider(HUDHandlerBC3Energy.INSTANCE, IPowerReceptor);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC3] Error while loading Energy hooks.", t);
        }

        try {
            ILiquidContainer = AccessHelper.getClass("buildcraft.api.ILiquidContainer");
            ILiquidContainer_getLiquidSlots = AccessHelper.getMethod(ILiquidContainer, new Class[0],
                    "getLiquidSlots", "getContents");
            ILiquidContainer_getLiquidId = AccessHelper.getMethod(ILiquidContainer, new Class[0], "getLiquidId");
            ILiquidContainer_getLiquidQuantity = AccessHelper.getMethod(ILiquidContainer, new Class[0],
                    "getLiquidQuantity");

            LiquidSlot = AccessHelper.getClass("buildcraft.api.LiquidSlot");
            newLiquidSlot = AccessHelper.getConstructor(LiquidSlot, int.class, int.class, int.class);
            LiquidSlot_getLiquidId = AccessHelper.getMethod(LiquidSlot, new Class[0], "getLiquidId");
            LiquidSlot_getLiquidQty = AccessHelper.getMethod(LiquidSlot, new Class[0], "getLiquidQty");
            LiquidSlot_getCapacity = AccessHelper.getMethod(LiquidSlot, new Class[0], "getCapacity");

            registrar.registerNBTProvider(HUDHandlerBC3Tanks.INSTANCE, ILiquidContainer);
            registrar.registerNBTProvider(HUDHandlerEntityBC3Tanks.INSTANCE, ILiquidContainer);

            registrar.registerHeadProvider(HUDHandlerBC3Tanks.INSTANCE, ILiquidContainer);
            registrar.registerHeadProvider(HUDHandlerBC3Tanks.INSTANCE, BlockCauldron.class);
            registrar.registerHeadProvider(HUDHandlerEntityBC3Tanks.INSTANCE, ILiquidContainer);

            registrar.registerBodyProvider(HUDHandlerBC3Tanks.INSTANCE, ILiquidContainer);
            registrar.registerBodyProvider(HUDHandlerBC3Tanks.INSTANCE, BlockCauldron.class);
            registrar.registerBodyProvider(HUDHandlerEntityBC3Tanks.INSTANCE, ILiquidContainer);

            registrar.addSyncedConfig("Buildcraft", "bc.tankamount");
            registrar.addSyncedConfig("Buildcraft", "bc.tanktype");

            registrar.addConfig("Buildcraft", "bcapi.liquidbars");
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC3] Error while loading Tank hooks.", t);
        }

        try {
            TileGenericPipe = AccessHelper.getClass("buildcraft.transport.TileGenericPipe");
            TileGenericPipe_pipe = AccessHelper.getField(TileGenericPipe, "pipe");

            Pipe = AccessHelper.getClass("buildcraft.transport.Pipe");
            Pipe_itemID = AccessHelper.getField(Pipe, "itemID");

            registrar.registerStackProvider(HUDHandlerBC3Pipes.INSTANCE, TileGenericPipe);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[BC3] Error while loading Pipe hooks.", t);
        }
    }

}
