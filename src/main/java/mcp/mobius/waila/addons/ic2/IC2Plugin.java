package mcp.mobius.waila.addons.ic2;

import cpw.mods.fml.relauncher.Side;
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
    public static Class<?> IEnergySink;
    public static Class<?> IEnergySource;
    public static Method IEnergyStorage_getStored;
    public static Method IEnergyStorage_getCapacity;
    public static Method IEnergySink_getInput;
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

    public static Class<?> TECrop;

    public static Class<?> TileEntityCrop;
    public static Field TileEntityCrop_id;
    public static Method TileEntityCrop_crop;

    public static Class<?> CropCard;
    public static Method CropCard_canBeHarvested;
    public static Method CropCard_getGain;
    public static Method CropCard_name;

    private IC2Plugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            AccessHelper.getClass("ic2.core.IC2");
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

            IEnergySink = AccessHelper.getClass("ic2.api.energy.tile.IEnergySink");
            IEnergySink_getInput = AccessHelper.getMethod(IEnergySink, new Class[0],
                    "getMaxSafeInput");

            IEnergySource = AccessHelper.getClass("ic2.api.energy.tile.IEnergySource");
            IEnergySource_getOutput = AccessHelper.getMethod(IEnergySource, new Class[0],
                    "getMaxEnergyOutput");

            TileBaseGenerator = AccessHelper.getClass("ic2.core.block.generator.tileentity.TileEntityBaseGenerator");
            TileBaseGenerator_storage = AccessHelper.getField(TileBaseGenerator, "storage");
            TileBaseGenerator_maxStorage = AccessHelper.getField(TileBaseGenerator, "maxStorage");

            TileEntityElecMachine = AccessHelper.getClass(
                    "ic2.core.block.machine.tileentity.TileEntityElecMachine");
            TileEntityElecMachine_maxEnergy = AccessHelper.getField(TileEntityElecMachine, "maxEnergy");

            registrar.addSyncedConfig("IndustrialCraft2", "ic2.inputeumach");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.inputeuother");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.outputeu");
            registrar.addSyncedConfig("IndustrialCraft2", "ic2.storage");

            if (side.isClient())
                registrar.addConfig("IndustrialCraft2", "ic2.energybars");

            registrar.registerNBTProvider(HUDHandlerElecMachine.INSTANCE, TileEntityElecMachine);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, IEnergyStorage);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergySink.INSTANCE, IEnergySink);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergySource.INSTANCE, IEnergySource);
            registrar.registerNBTProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, TileBaseGenerator);

            if (side.isClient()) {
                registrar.registerBodyProvider(HUDHandlerElecMachine.INSTANCE, TileEntityElecMachine);
                registrar.registerBodyProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, IEnergyStorage);
                registrar.registerBodyProvider(HUDHandlerIC2IEnergySink.INSTANCE, IEnergySink);
                registrar.registerBodyProvider(HUDHandlerIC2IEnergySource.INSTANCE, IEnergySource);
                registrar.registerBodyProvider(HUDHandlerIC2IEnergyStorage.INSTANCE, TileBaseGenerator);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading energy API hooks.", t);
        }

        try {
            TileEntityMatter = AccessHelper.getClass("ic2.core.block.machine.tileentity.TileEntityMatter");
            TileEntityMatter_getProgressAsString = AccessHelper.getMethod(TileEntityMatter, new Class[0],
                    "getProgressAsString");

            registrar.addSyncedConfig("IndustrialCraft2", "ic2.mattergen");

            registrar.registerNBTProvider(HUDHandlerMatterGen.INSTANCE, TileEntityMatter);

            if (side.isClient()) {
                registrar.registerBodyProvider(HUDHandlerMatterGen.INSTANCE, TileEntityMatter);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading matter gen hooks.", t);
        }

        try {
            EntityIC2Explosive = AccessHelper.getClass("ic2.core.block.EntityIC2Explosive");
            EntityIC2Explosive_fuse = AccessHelper.getField(EntityIC2Explosive, "fuse");
            EntityIC2Explosive_renderBlock = AccessHelper.getField(EntityIC2Explosive, "renderBlock");

            registrar.registerNBTProvider(HUDHandlerIC2Explosive.INSTANCE, EntityIC2Explosive);

            if (side.isClient()) {
                registrar.registerStackProvider(HUDHandlerIC2Explosive.INSTANCE, EntityIC2Explosive);

                registrar.registerHeadProvider(HUDHandlerIC2Explosive.INSTANCE, EntityIC2Explosive);

                registrar.registerBodyProvider(HUDHandlerIC2Explosive.INSTANCE, EntityIC2Explosive);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading TNT hooks.", t);
        }

        try {
            TECrop = AccessHelper.getClass("ic2.api.TECrop");

            TileEntityCrop = AccessHelper.getClass("ic2.core.block.TileEntityCrop");
            TileEntityCrop_id = AccessHelper.getField(TileEntityCrop, "id");
            TileEntityCrop_crop = AccessHelper.getMethod(TileEntityCrop, new Class[0], "crop");

            CropCard = AccessHelper.getClass("ic2.api.CropCard");
            CropCard_canBeHarvested = AccessHelper.getMethod(CropCard, new Class[]{TECrop}, "canBeHarvested");
            CropCard_getGain = AccessHelper.getMethod(CropCard, new Class[]{TECrop}, "getGain");
            CropCard_name = AccessHelper.getMethod(CropCard, new Class[0], "name");

            if (side.isClient()) {
                registrar.addConfig("IndustrialCraft2", "ic2.crop");

                registrar.registerBodyProvider(HUDHandlerIC2Crop.DATA_PROVIDER, TileEntityCrop);

                registrar.registerCropProvider(HUDHandlerIC2Crop.CROP_PROVIDER, TileEntityCrop);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft 2] Error while loading crop hooks.", t);
        }
    }

}
