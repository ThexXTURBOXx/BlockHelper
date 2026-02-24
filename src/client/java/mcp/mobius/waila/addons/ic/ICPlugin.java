package mcp.mobius.waila.addons.ic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.Block;
import net.minecraft.src.BlockContainer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;
import net.minecraft.src.mod_BlockHelper;

public final class ICPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new ICPlugin();

    public static Class<?> mod_IndustrialCraft;

    private ICPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            mod_IndustrialCraft = AccessHelper.getClass("mod_IndustrialCraft");
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft] Mod found.");
            return true;
        } catch (Throwable ignored) {
            mod_BlockHelper.LOG.log(Level.INFO, "[IndustrialCraft] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar) {
        // XXX: We register the Energy interface first
        try {
            registrar.addSyncedConfig("IndustrialCraft", "ic.storage");

            registrar.addConfig("IndustrialCraft", "ic.energybars");

            Set<Class<?>> registeredTEs = new HashSet<Class<?>>();
            for (Field f : mod_IndustrialCraft.getFields()) {
                if (!Modifier.isStatic(f.getModifiers())) continue;

                Object obj = f.get(null);
                if (obj == null || !BlockContainer.class.isAssignableFrom(obj.getClass())) continue;

                TileEntity te = mod_BlockHelper.Accessor.getBlockEntity((BlockContainer) obj);
                if (te == null) continue;

                try {
                    Class<?> clazz = te.getClass();
                    if (!registeredTEs.add(clazz)) continue;

                    Field currCharge = AccessHelper.getDeclaredField(clazz, "chargePoints", "furnaceBurnTime");
                    Field maxCharge = null;
                    try {
                        maxCharge = AccessHelper.getDeclaredField(clazz, "chargened", "powerStorage");
                    } catch (Throwable ignored) {
                    }

                    IDataProvider provider = new HUDHandlerICCharge(currCharge, maxCharge);

                    registrar.registerNBTProvider(provider, clazz);

                    registrar.registerBodyProvider(provider, clazz);
                } catch (Throwable ignored) {
                }
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft] Error while loading energy hooks.", t);
        }

        try {
            String[] tntClasses = new String[]{
                    "EntityItntPrimed",
                    "EntityNtntPrimed"
            };
            Block[] tntBlocks = new Block[]{
                    (Block) AccessHelper.getField(mod_IndustrialCraft, "blockItnt").get(null),
                    (Block) AccessHelper.getField(mod_IndustrialCraft, "blockNtnt").get(null)
            };
            for (int i = 0; i < tntClasses.length; ++i) {
                try {
                    Class<?> clazz = AccessHelper.getClass(tntClasses[i]);

                    Field fuse = AccessHelper.getDeclaredField(clazz, "a");

                    IEntityProvider provider = new HUDHandlerICtntPrimed(fuse, new ItemStack(tntBlocks[i]));

                    registrar.registerNBTProvider(provider, clazz);

                    registrar.registerStackProvider(provider, clazz);

                    registrar.registerHeadProvider(provider, clazz);

                    registrar.registerBodyProvider(provider, clazz);
                } catch (Throwable t) {
                    mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft] Error while loading TNT hooks.", t);
                }
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[IndustrialCraft] Error while loading TNT hooks.", t);
        }
    }

}
