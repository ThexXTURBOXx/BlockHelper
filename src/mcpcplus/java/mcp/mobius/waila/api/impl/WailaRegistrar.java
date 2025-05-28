package mcp.mobius.waila.api.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.utils.Constants;
import net.minecraft.src.Entity;

public class WailaRegistrar implements IRegistrar {

    private static WailaRegistrar instance = null;

    public final Map<Class<?>, List<IDataProvider>> NBTDataProviders =
            new LinkedHashMap<Class<?>, List<IDataProvider>>();

    public final Map<Class<?>, List<IEntityProvider>> NBTEntityProviders =
            new LinkedHashMap<Class<?>, List<IEntityProvider>>();

    private WailaRegistrar() {
        instance = this;
    }

    public static WailaRegistrar instance() {
        return instance == null ? new WailaRegistrar() : instance;
    }

    /* CONFIG HANDLING */
    @Override
    public void addSyncedConfig(String modName, String key) {
        this.addSyncedConfig(modName, key, Constants.CFG_DEFAULT_VALUE);
    }

    @Override
    public void addSyncedConfig(String modName, String key, boolean defValue) {
        PluginConfig.instance().addSyncedConfig(modName, key, defValue);
    }


    /* REGISTRATION METHODS */
    @Override
    public void registerNBTProvider(IDataProvider dataProvider, Class<?> block) {
        this.registerProvider(dataProvider, block, this.NBTDataProviders);
    }

    @Override
    public void registerNBTProvider(IEntityProvider dataProvider, Class<?> entity) {
        this.registerProvider(dataProvider, entity, this.NBTEntityProviders);
    }

    private <T, V> void registerProvider(T dataProvider, V clazz, Map<V, List<T>> target) {
        if (clazz == null || dataProvider == null)
            throw new RuntimeException(String.format(
                    "Trying to register a null provider or null block! Please check the stacktrace to know what " +
                    "was the original registration method. [Provider: %s, Target: %s]",
                    dataProvider == null ? "null" : dataProvider.getClass().getName(), clazz));

        if (!target.containsKey(clazz))
            target.put(clazz, new ArrayList<T>());

        List<T> providers = target.get(clazz);
        if (!providers.contains(dataProvider))
            target.get(clazz).add(dataProvider);
    }

    /* PROVIDER GETTERS */
    public Map<Integer, List<IDataProvider>> getNBTProviders(Object block) {
        return getProviders(block, this.NBTDataProviders);
    }

    public Map<Integer, List<IEntityProvider>> getNBTEntityProviders(Object entity) {
        return getProviders(entity, this.NBTEntityProviders);
    }

    private <V, T> Map<Integer, List<T>> getProviders(V obj, Map<Class<? extends V>, List<T>> target) {
        Map<Integer, List<T>> returnList = new TreeMap<Integer, List<T>>();

        int index = 0;
        for (Class<?> clazz : target.keySet()) {
            if (clazz.isInstance(obj))
                returnList.put(index, target.get(clazz));
            ++index;
        }

        return returnList;
    }

    /* HAS METHODS */
    public boolean hasNBTProviders(Object block) {
        return hasProviders(block, this.NBTDataProviders);
    }

    public boolean hasNBTEntityProviders(Entity entity) {
        return hasProviders(entity, this.NBTEntityProviders);
    }

    private <V, T> boolean hasProviders(Object obj, Map<Class<? extends V>, List<T>> target) {
        for (Class<?> clazz : target.keySet())
            if (clazz.isInstance(obj))
                return true;
        return false;
    }

}
