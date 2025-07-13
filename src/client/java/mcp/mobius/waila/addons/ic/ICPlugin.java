package mcp.mobius.waila.addons.ic;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.BlockContainer;
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
    }

}
