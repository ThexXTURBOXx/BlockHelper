package mcp.mobius.waila.addons.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import mcp.mobius.waila.api.ICropProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.StringUtils;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;

public final class HUDHandlerCrops implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerCrops();

    private final WailaRegistrar registrar = WailaRegistrar.instance();

    private HUDHandlerCrops() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
        Block block = accessor.getBlock();

        if (config.get("general.showcrop")) {
            ICropProvider provider = getProvider(block, accessor);
            if (provider != null)
                currenttip.addAll(provider.getGrowthDetails(itemStack, accessor, config));
        }
    }

    private ICropProvider getProvider(Block b, IDataAccessor accessor) {
        ICropProvider provider = null;

        // Since TEs are usually more specific, they take precedence here
        if (registrar.hasCropProvider(accessor.getTileEntity())) {
            Class<?> providerClass = TileEntity.class;
            for (Class<?> clazz : registrar.cropProviders.keySet()) {
                if (clazz.isInstance(accessor.getTileEntity()) && providerClass.isAssignableFrom(clazz)) {
                    List<ICropProvider> providers = registrar.cropProviders.get(clazz);
                    if (!providers.isEmpty()) {
                        provider = registrar.cropProviders.get(clazz).get(0);
                        providerClass = clazz;
                    }
                }
            }
            if (provider != null) return provider;
        }

        if (registrar.hasCropProvider(b)) {
            Class<?> providerClass = Block.class;
            for (Class<?> clazz : registrar.cropProviders.keySet()) {
                if (clazz.isInstance(b) && providerClass.isAssignableFrom(clazz)) {
                    List<ICropProvider> providers = registrar.cropProviders.get(clazz);
                    if (!providers.isEmpty()) {
                        provider = registrar.cropProviders.get(clazz).get(0);
                        providerClass = clazz;
                    }
                }
            }
            if (provider != null) return provider;
        }

        try {
            for (Method method : b.getClass().getDeclaredMethods()) {
                String name = method.getName();
                if (name.equals("getGrowthRate") ||
                    name.equals("getGrowthModifier")) {
                    provider = new DefaultCropProvider(tryGetMaxStage(b, accessor.getBlockID()));
                    registrar.registerCropProvider(provider, b.getClass());
                }
            }
        } catch (Throwable ignored) {
        }

        return provider;
    }

    private int tryGetMaxStage(Block b, int id) {
        try {
            for (Field field : b.getClass().getFields()) {
                if (StringUtils.containsIgnoreCase(field.getName(), "max")
                    && StringUtils.containsIgnoreCase(field.getName(), "stage")) {
                    return field.getInt(Block.blocksList[id]);
                }
            }
            for (Field field : b.getClass().getDeclaredFields()) {
                if (StringUtils.containsIgnoreCase(field.getName(), "max")
                    && StringUtils.containsIgnoreCase(field.getName(), "stage")) {
                    field.setAccessible(true);
                    return field.getInt(Block.blocksList[id]);
                }
            }
        } catch (Throwable ignored) {
        }
        return 7;
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
    }

}
