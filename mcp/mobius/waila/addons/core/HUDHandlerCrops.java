package mcp.mobius.waila.addons.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import mcp.mobius.waila.api.ICropHandler;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.StringUtils;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

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
        /* Crops */
        if (config.get("general.showcrop")) {
            ICropHandler handler = getHandler(block, accessor.getBlockID());
            if (handler != null)
                currenttip.addAll(handler.getGrowthString(itemStack, accessor, config));
        }
    }

    private ICropHandler getHandler(Block b, int id) {
        Class<?> handlerClass = Block.class;
        ICropHandler handler = null;
        if (registrar.hasCropHandler(b)) {
            for (Class<?> clazz : registrar.cropHandlers.keySet()) {
                if (clazz.isInstance(b) && handlerClass.isAssignableFrom(clazz)) {
                    List<ICropHandler> handlers = registrar.cropHandlers.get(clazz);
                    if (!handlers.isEmpty())
                        handler = registrar.cropHandlers.get(clazz).get(0);
                }
            }
        }
        if (handler != null) return handler;

        try {
            for (Method method : b.getClass().getDeclaredMethods()) {
                String name = method.getName();
                if (name.equals("getGrowthRate") ||
                    name.equals("getGrowthModifier")) {
                    handler = new DefaultCropHandler(tryGetMaxStage(b, id));
                    registrar.registerCropHandler(handler, b.getClass());
                }
            }
        } catch (Throwable ignored) {
        }

        return handler;
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
