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

    public static boolean isIC2Mp = false;

    public static Class<?> IEnergySource;
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
    public static Field EntityIC2Explosive_renderBlock;

    public static Class<?> TileEntityCable;

    public static Class<?> mod_IC2;
    public static Field mod_IC2_itemCable;

    private IC2Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            mod_IC2 = AccessHelper.getClass("mod_IC2Mp");
            isIC2Mp = true;
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft 2] Mp Mod found.");
            return true;
        } catch (Throwable ignored) {
        }
        try {
            mod_IC2 = AccessHelper.getClass("mod_IC2");
            isIC2Mp = false;
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft 2] Mod found.");
            return true;
        } catch (Throwable ignored) {
        }
        mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft 2] Mod not found.");
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        String apiPkg = "ic2." + (isIC2Mp ? "api." : "");
        String commonPkg = "ic2." + (isIC2Mp ? "common." : "");

        // XXX: We register the Energy interface first
        try {
            IEnergySource = AccessHelper.getClass(apiPkg + "IEnergySource");
            IEnergySource_getOutput = AccessHelper.getMethod(IEnergySource, new Class[0],
                    "getMaxEnergyOutput");

            TileBaseGenerator = AccessHelper.getClass(commonPkg + "TileEntityBaseGenerator");
            TileBaseGenerator_storage = AccessHelper.getField(TileBaseGenerator, "storage");
            TileBaseGenerator_maxStorage = AccessHelper.getField(TileBaseGenerator, "maxStorage");

            TileEntityElecMachine = AccessHelper.getClass(commonPkg + "TileEntityElecMachine");
            TileEntityElecMachine_maxEnergy = AccessHelper.getField(TileEntityElecMachine, "maxEnergy");

            registrar.addSyncedConfig("IndustrialCraft2", "ic2.inputeumach");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.inputeuother");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.outputeu");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.storage");

            registrar.registerNBTProvider(HUDHandlerElecMachine.INSTANCE, TileEntityElecMachine);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergySource.INSTANCE, IEnergySource);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, TileBaseGenerator);

            registrar.registerBodyProvider(HUDHandlerElecMachine.INSTANCE, TileEntityElecMachine);
            registrar.registerBodyProvider(HUDHandlerIC2IEnergySource.INSTANCE, IEnergySource);
            registrar.registerBodyProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, TileBaseGenerator);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading energy API hooks.", t);
        }

        try {
            TileEntityMatter = AccessHelper.getClass(commonPkg + "TileEntityMatter");
            TileEntityMatter_getProgressAsString = AccessHelper.getMethod(TileEntityMatter, new Class[0],
                    "getProgressAsString");

            registrar.addSyncedConfig("IndustrialCraft2", "ic2.mattergen");

            registrar.registerNBTProvider(HUDHandlerMatterGen.INSTANCE, TileEntityMatter);

            registrar.registerBodyProvider(HUDHandlerMatterGen.INSTANCE, TileEntityMatter);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading matter gen hooks.", t);
        }

        try {
            EntityIC2Explosive = AccessHelper.getClass(commonPkg + "EntityIC2Explosive");
            EntityIC2Explosive_fuse = AccessHelper.getField(EntityIC2Explosive, "fuse");
            EntityIC2Explosive_renderBlock = AccessHelper.getField(EntityIC2Explosive, "renderBlock");

            registrar.registerNBTProvider(HUDHandlerIC2Explosive.INSTANCE, EntityIC2Explosive);

            registrar.registerStackProvider(HUDHandlerIC2Explosive.INSTANCE, EntityIC2Explosive);

            registrar.registerHeadProvider(HUDHandlerIC2Explosive.INSTANCE, EntityIC2Explosive);

            registrar.registerBodyProvider(HUDHandlerIC2Explosive.INSTANCE, EntityIC2Explosive);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading TNT hooks.", t);
        }

        try {
            TileEntityCable = AccessHelper.getClass(commonPkg + "TileEntityCable");

            mod_IC2_itemCable = AccessHelper.getField(mod_IC2, "itemCable");

            registrar.registerStackProvider(HUDHandlerIC2Cable.INSTANCE, TileEntityCable);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading cable hooks.", t);
        }
    }

}
