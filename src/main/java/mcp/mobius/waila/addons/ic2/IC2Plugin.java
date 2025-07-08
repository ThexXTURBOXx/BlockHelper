package mcp.mobius.waila.addons.ic2;

import cpw.mods.fml.common.Side;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.mod_BlockHelper;

public final class IC2Plugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new IC2Plugin();

    public static Class<?> IEnergyStorage;
    public static Class<?> IEnergySource;
    public static Method IEnergyStorage_getStored;
    public static Method IEnergyStorage_getCapacity;
    public static Method IEnergySource_getOutput;

    public static Class<?> TileBaseGenerator;
    public static Field TileBaseGenerator_storage;
    public static Field TileBaseGenerator_maxStorage;

    public static Class<?> TileEntityElecMachine;
    public static Field TileEntityElecMachine_maxEnergy;

    private IC2Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("ic2.common.IC2");
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft 2] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft 2] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        // XXX: We register the Energy interface first
        try {
            IEnergyStorage = AccessHelper.getClass("ic2.api.IEnergyStorage");
            IEnergyStorage_getStored = AccessHelper.getMethod(IEnergyStorage, new Class[0],
                    "getStored");
            IEnergyStorage_getCapacity = AccessHelper.getMethod(IEnergyStorage, new Class[0],
                    "getCapacity");

            IEnergySource = AccessHelper.getClass("ic2.api.IEnergySource");
            IEnergySource_getOutput = AccessHelper.getMethod(IEnergySource, new Class[0],
                    "getMaxEnergyOutput");

            TileBaseGenerator = AccessHelper.getClass("ic2.common.TileEntityBaseGenerator");
            TileBaseGenerator_storage = AccessHelper.getField(TileBaseGenerator, "storage");
            TileBaseGenerator_maxStorage = AccessHelper.getField(TileBaseGenerator, "maxStorage");

            TileEntityElecMachine = AccessHelper.getClass("ic2.common.TileEntityElecMachine");
            TileEntityElecMachine_maxEnergy = AccessHelper.getField(TileEntityElecMachine, "maxEnergy");

            registrar.addSyncedConfig("IndustrialCraft2", "ic2.inputeumach");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.inputeuother");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.outputeu");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.storage");

            registrar.registerNBTProvider(HUDHandlerElecMachine.INSTANCE, TileEntityElecMachine);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, IEnergyStorage);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergySource.INSTANCE, IEnergySource);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, TileBaseGenerator);

            if (side.isClient()) {
                registrar.registerBodyProvider(HUDHandlerElecMachine.INSTANCE, TileEntityElecMachine);
                registrar.registerBodyProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, IEnergyStorage);
                registrar.registerBodyProvider(HUDHandlerIC2IEnergySource.INSTANCE, IEnergySource);
                registrar.registerBodyProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, TileBaseGenerator);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading energy API hooks.", t);
        }
    }

}
