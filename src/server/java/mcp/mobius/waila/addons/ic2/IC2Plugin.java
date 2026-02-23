package mcp.mobius.waila.addons.ic2;

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

    public static Class<?> TileEntityMatter;
    public static Method TileEntityMatter_getProgressAsString;

    public static Class<?> EntityIC2Explosive;
    public static Field EntityIC2Explosive_fuse;

    private IC2Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("mod_IC2");
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft 2] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft 2] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        // XXX: We register the Energy interface first

        boolean storageRegistered = false;

        try {
            // We do this one in an extra block as it is not available in early IC² versions for 1.2.4/5...
            IEnergyStorage = AccessHelper.getClass("ic2.api.IEnergyStorage");
            IEnergyStorage_getStored = AccessHelper.getMethod(IEnergyStorage, new Class[0],
                    "getStored");
            IEnergyStorage_getCapacity = AccessHelper.getMethod(IEnergyStorage, new Class[0],
                    "getCapacity");

            registrar.registerNBTProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, IEnergyStorage);

            storageRegistered = true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.FINE, "[IndustrialCraft 2] Skipping registration of IEnergyStorage.", t);
        }

        try {
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

            registrar.registerNBTProvider(HUDHandlerElecMachine.INSTANCE, TileEntityElecMachine);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergySource.INSTANCE, IEnergySource);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, TileBaseGenerator);

            storageRegistered = true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading energy API hooks.", t);
        }

        if (storageRegistered) {
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.storage");
        }

        try {
            TileEntityMatter = AccessHelper.getClass("ic2.common.TileEntityMatter");
            TileEntityMatter_getProgressAsString = AccessHelper.getMethod(TileEntityMatter, new Class[0],
                    "getProgressAsString");

            registrar.addSyncedConfig("IndustrialCraft2", "ic2.mattergen");

            registrar.registerNBTProvider(HUDHandlerMatterGen.INSTANCE, TileEntityMatter);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading matter gen hooks.", t);
        }

        try {
            EntityIC2Explosive = AccessHelper.getClass("ic2.common.EntityIC2Explosive");
            EntityIC2Explosive_fuse = AccessHelper.getField(EntityIC2Explosive, "fuse");

            registrar.registerNBTProvider(HUDHandlerIC2Explosive.INSTANCE, EntityIC2Explosive);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading TNT hooks.", t);
        }
    }

}
