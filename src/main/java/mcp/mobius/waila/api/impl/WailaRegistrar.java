package mcp.mobius.waila.api.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import mcp.mobius.waila.api.IBlockDecorator;
import mcp.mobius.waila.api.ICropProvider;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
import mcp.mobius.waila.api.IFMPDecorator;
import mcp.mobius.waila.api.IFMPProvider;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.Constants;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;

public class WailaRegistrar implements IRegistrar {

    private static WailaRegistrar instance = null;

    public final Map<Class<?>, List<IDataProvider>> headBlockProviders =
            new LinkedHashMap<Class<?>, List<IDataProvider>>();
    public final Map<Class<?>, List<IDataProvider>> bodyBlockProviders =
            new LinkedHashMap<Class<?>, List<IDataProvider>>();
    public final Map<Class<?>, List<IDataProvider>> tailBlockProviders =
            new LinkedHashMap<Class<?>, List<IDataProvider>>();
    public final Map<Class<?>, List<IDataProvider>> stackBlockProviders =
            new LinkedHashMap<Class<?>, List<IDataProvider>>();
    public final Map<Class<?>, List<IDataProvider>> NBTDataProviders =
            new LinkedHashMap<Class<?>, List<IDataProvider>>();

    public final Map<Class<?>, List<IBlockDecorator>> blockClassDecorators =
            new LinkedHashMap<Class<?>, List<IBlockDecorator>>();

    public final Map<Class<?>, List<IEntityProvider>> headEntityProviders =
            new LinkedHashMap<Class<?>, List<IEntityProvider>>();
    public final Map<Class<?>, List<IEntityProvider>> bodyEntityProviders =
            new LinkedHashMap<Class<?>, List<IEntityProvider>>();
    public final Map<Class<?>, List<IEntityProvider>> tailEntityProviders =
            new LinkedHashMap<Class<?>, List<IEntityProvider>>();
    public final Map<Class<?>, List<IEntityProvider>> overrideEntityProviders =
            new LinkedHashMap<Class<?>, List<IEntityProvider>>();
    public final Map<Class<?>, List<IEntityProvider>> stackEntityProviders =
            new LinkedHashMap<Class<?>, List<IEntityProvider>>();
    public final Map<Class<?>, List<IEntityProvider>> NBTEntityProviders =
            new LinkedHashMap<Class<?>, List<IEntityProvider>>();

    public final Map<String, List<IFMPProvider>> headFMPProviders =
            new LinkedHashMap<String, List<IFMPProvider>>();
    public final Map<String, List<IFMPProvider>> bodyFMPProviders =
            new LinkedHashMap<String, List<IFMPProvider>>();
    public final Map<String, List<IFMPProvider>> tailFMPProviders =
            new LinkedHashMap<String, List<IFMPProvider>>();

    public final Map<String, List<IFMPDecorator>> FMPClassDecorators =
            new LinkedHashMap<String, List<IFMPDecorator>>();

    public final Map<Class<?>, List<ICropProvider>> cropProviders =
            new LinkedHashMap<Class<?>, List<ICropProvider>>();

    public final Map<String, ITooltipRenderer> tooltipRenderers =
            new LinkedHashMap<String, ITooltipRenderer>();

    private WailaRegistrar() {
        instance = this;
    }

    public static WailaRegistrar instance() {
        return instance == null ? new WailaRegistrar() : instance;
    }

    /* CONFIG HANDLING */
    @Override
    public void addConfig(String modName, String key, String translationKey) {
        this.addConfig(modName, key, translationKey, Constants.CFG_DEFAULT_VALUE);
    }

    @Override
    public void addSyncedConfig(String modName, String key, String translationKey) {
        this.addSyncedConfig(modName, key, translationKey, Constants.CFG_DEFAULT_VALUE);
    }

    @Override
    public void addConfig(String modName, String key) {
        this.addConfig(modName, key, Constants.CFG_DEFAULT_VALUE);
    }

    @Override
    public void addSyncedConfig(String modName, String key) {
        this.addSyncedConfig(modName, key, Constants.CFG_DEFAULT_VALUE);
    }

    @Override
    public void addConfig(String modName, String key, boolean defValue) {
        this.addConfig(modName, key, "option." + key, defValue);
    }

    @Override
    public void addSyncedConfig(String modName, String key, boolean defValue) {
        this.addSyncedConfig(modName, key, "option." + key, defValue);
    }

    @Override
    public void addConfig(String modName, String key, String translationKey, boolean defValue) {
        PluginConfig.instance().addConfig(modName, key, translationKey, defValue);
    }

    @Override
    public void addSyncedConfig(String modName, String key, String translationKey, boolean defValue) {
        PluginConfig.instance().addSyncedConfig(modName, key, translationKey, defValue);
    }


    /* REGISTRATION METHODS */
    @Override
    public void registerHeadProvider(IDataProvider dataProvider, Class<?> block) {
        this.registerProvider(dataProvider, block, this.headBlockProviders);
    }

    @Override
    public void registerBodyProvider(IDataProvider dataProvider, Class<?> block) {
        this.registerProvider(dataProvider, block, this.bodyBlockProviders);
    }

    @Override
    public void registerTailProvider(IDataProvider dataProvider, Class<?> block) {
        this.registerProvider(dataProvider, block, this.tailBlockProviders);
    }

    @Override
    public void registerStackProvider(IDataProvider dataProvider, Class<?> block) {
        this.registerProvider(dataProvider, block, this.stackBlockProviders);
    }

    @Override
    public void registerNBTProvider(IDataProvider dataProvider, Class<?> block) {
        this.registerProvider(dataProvider, block, this.NBTDataProviders);
    }

    @Override
    public void registerHeadProvider(IEntityProvider dataProvider, Class<?> entity) {
        this.registerProvider(dataProvider, entity, this.headEntityProviders);
    }

    @Override
    public void registerBodyProvider(IEntityProvider dataProvider, Class<?> entity) {
        this.registerProvider(dataProvider, entity, this.bodyEntityProviders);
    }

    @Override
    public void registerTailProvider(IEntityProvider dataProvider, Class<?> entity) {
        this.registerProvider(dataProvider, entity, this.tailEntityProviders);
    }

    @Override
    public void registerStackProvider(IEntityProvider dataProvider, Class<?> entity) {
        this.registerProvider(dataProvider, entity, this.stackEntityProviders);
    }

    @Override
    public void registerNBTProvider(IEntityProvider dataProvider, Class<?> entity) {
        this.registerProvider(dataProvider, entity, this.NBTEntityProviders);
    }

    @Override
    public void registerHeadProvider(IFMPProvider dataProvider, String name) {
        this.registerProvider(dataProvider, name, this.headFMPProviders);
    }

    @Override
    public void registerBodyProvider(IFMPProvider dataProvider, String name) {
        this.registerProvider(dataProvider, name, this.bodyFMPProviders);
    }

    @Override
    public void registerTailProvider(IFMPProvider dataProvider, String name) {
        this.registerProvider(dataProvider, name, this.tailFMPProviders);
    }

    @Override
    public void registerOverrideEntityProvider(IEntityProvider dataProvider, Class<?> entity) {
        this.registerProvider(dataProvider, entity, this.overrideEntityProviders);
    }

    @Override
    public void registerDecorator(IBlockDecorator decorator, Class<?> block) {
        this.registerProvider(decorator, block, this.blockClassDecorators);
    }

    @Override
    public void registerDecorator(IFMPDecorator decorator, String name) {
        this.registerProvider(decorator, name, this.FMPClassDecorators);
    }

    @Override
    public void registerCropProvider(ICropProvider cropProvider, Class<?> block) {
        this.registerProvider(cropProvider, block, this.cropProviders);
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

    @Override
    public void registerTooltipRenderer(String name, ITooltipRenderer renderer) {
        if (!this.tooltipRenderers.containsKey(name))
            this.tooltipRenderers.put(name, renderer);
        else
            mod_BlockHelper.LOG.warning(String.format(
                    "A renderer named %s already exists (Class: %s). Skipping new renderer.",
                    name, renderer.getClass().getName()));
    }

    /* PROVIDER GETTERS */

    public Map<Integer, List<IDataProvider>> getHeadProviders(Object block) {
        return getProviders(block, this.headBlockProviders);
    }

    public Map<Integer, List<IDataProvider>> getBodyProviders(Object block) {
        return getProviders(block, this.bodyBlockProviders);
    }

    public Map<Integer, List<IDataProvider>> getTailProviders(Object block) {
        return getProviders(block, this.tailBlockProviders);
    }

    public Map<Integer, List<IDataProvider>> getStackProviders(Object block) {
        return getProviders(block, this.stackBlockProviders);
    }

    public Map<Integer, List<IDataProvider>> getNBTProviders(Object block) {
        return getProviders(block, this.NBTDataProviders);
    }

    public Map<Integer, List<IEntityProvider>> getHeadEntityProviders(Object entity) {
        return getProviders(entity, this.headEntityProviders);
    }

    public Map<Integer, List<IEntityProvider>> getBodyEntityProviders(Object entity) {
        return getProviders(entity, this.bodyEntityProviders);
    }

    public Map<Integer, List<IEntityProvider>> getTailEntityProviders(Object entity) {
        return getProviders(entity, this.tailEntityProviders);
    }

    public Map<Integer, List<IEntityProvider>> getOverrideEntityProviders(Object entity) {
        return getProviders(entity, this.overrideEntityProviders);
    }

    public Map<Integer, List<IEntityProvider>> getStackEntityProviders(Object entity) {
        return getProviders(entity, this.stackEntityProviders);
    }

    public Map<Integer, List<IEntityProvider>> getNBTEntityProviders(Object entity) {
        return getProviders(entity, this.NBTEntityProviders);
    }

    public Map<Integer, List<IFMPProvider>> getHeadFMPProviders(String name) {
        return getProviders(name, this.headFMPProviders);
    }

    public Map<Integer, List<IFMPProvider>> getBodyFMPProviders(String name) {
        return getProviders(name, this.bodyFMPProviders);
    }

    public Map<Integer, List<IFMPProvider>> getTailFMPProviders(String name) {
        return getProviders(name, this.tailFMPProviders);
    }

    public Map<Integer, List<IBlockDecorator>> getBlockDecorators(Object block) {
        return getProviders(block, this.blockClassDecorators);
    }

    public Map<Integer, List<IFMPDecorator>> getFMPDecorators(String name) {
        return getProviders(name, this.FMPClassDecorators);
    }

    public Map<Integer, List<ICropProvider>> getCropProviders(Object block) {
        return getProviders(block, this.cropProviders);
    }

    public ITooltipRenderer getTooltipRenderer(String name) {
        return this.tooltipRenderers.get(name);
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

    private <T> Map<Integer, List<T>> getProviders(String name, Map<String, List<T>> target) {
        Map<Integer, List<T>> returnList = new TreeMap<Integer, List<T>>();
        returnList.put(0, target.get(name));
        return returnList;
    }

    /* HAS METHODS */

    public boolean hasStackProviders(Object block) {
        return hasProviders(block, this.stackBlockProviders);
    }

    public boolean hasHeadProviders(Object block) {
        return hasProviders(block, this.headBlockProviders);
    }

    public boolean hasBodyProviders(Object block) {
        return hasProviders(block, this.bodyBlockProviders);
    }

    public boolean hasTailProviders(Object block) {
        return hasProviders(block, this.tailBlockProviders);
    }

    public boolean hasNBTProviders(Object block) {
        return hasProviders(block, this.NBTDataProviders);
    }

    public boolean hasStackEntityProviders(Entity entity) {
        return hasProviders(entity, this.stackEntityProviders);
    }

    public boolean hasHeadEntityProviders(Entity entity) {
        return hasProviders(entity, this.headEntityProviders);
    }

    public boolean hasBodyEntityProviders(Entity entity) {
        return hasProviders(entity, this.bodyEntityProviders);
    }

    public boolean hasTailEntityProviders(Entity entity) {
        return hasProviders(entity, this.tailEntityProviders);
    }

    public boolean hasOverrideEntityProviders(Entity entity) {
        return hasProviders(entity, this.overrideEntityProviders);
    }

    public boolean hasNBTEntityProviders(Entity entity) {
        return hasProviders(entity, this.NBTEntityProviders);
    }

    public boolean hasHeadFMPProviders(String name) {
        return hasProviders(name, this.headFMPProviders);
    }

    public boolean hasBodyFMPProviders(String name) {
        return hasProviders(name, this.bodyFMPProviders);
    }

    public boolean hasTailFMPProviders(String name) {
        return hasProviders(name, this.tailFMPProviders);
    }

    public boolean hasBlockDecorator(Block block) {
        return hasProviders(block, this.blockClassDecorators);
    }

    public boolean hasFMPDecorator(String name) {
        return hasProviders(name, this.FMPClassDecorators);
    }

    public boolean hasCropProvider(Object block) {
        return hasProviders(block, this.cropProviders);
    }

    private <V, T> boolean hasProviders(Object obj, Map<Class<? extends V>, List<T>> target) {
        for (Class<?> clazz : target.keySet())
            if (clazz.isInstance(obj))
                return true;
        return false;
    }

    private <T> boolean hasProviders(String name, Map<String, List<T>> target) {
        return target.containsKey(name);
    }

}
