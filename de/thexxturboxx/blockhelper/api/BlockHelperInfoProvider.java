package de.thexxturboxx.blockhelper.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class BlockHelperInfoProvider implements BlockHelperBlockProvider, BlockHelperTileEntityProvider,
        BlockHelperNameFixer, BlockHelperItemStackFixer, BlockHelperModFixer {

    private static final List<String> loadedCache = new ArrayList<String>();
    private static final List<String> loadedCacheFailed = new ArrayList<String>();

    public static boolean isLoadedAndInstanceOf(Object obj, String clazz) {
        if (obj == null || loadedCacheFailed.contains(clazz))
            return false;
        try {
            Class<?> c = Class.forName(clazz);
            if (!loadedCache.contains(clazz)) {
                loadedCache.add(clazz);
            }
            if (c.isInstance(obj))
                return true;
        } catch (Exception e) {
            loadedCacheFailed.add(clazz);
        }
        return false;
    }

    protected boolean iof(Object obj, String clazz) {
        return isLoadedAndInstanceOf(obj, clazz);
    }

    protected Method getMethod(Object obj, String method) {
        try {
            Method m = obj.getClass().getMethod(method);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T, U> T getDeclaredField(Class<U> clazz, String field) {
        try {
            Field f = clazz.getDeclaredField(field);
            f.setAccessible(true);
            return (T) f.get(null);
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T getDeclaredField(Object obj, String field) {
        try {
            Field f = obj.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return (T) f.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    protected <T> T getField(Object obj, String field) {
        try {
            Field f = obj.getClass().getField(field);
            f.setAccessible(true);
            return (T) f.get(obj);
        } catch (IllegalAccessException e) {
            return null;
        } catch (NoSuchFieldException e) {
            return null;
        }
    }

    @Override
    public void addInformation(Block block, int id, int meta, InfoHolder info) {
    }

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
    }

    @Override
    public String getName(Block block, TileEntity te, int id, int meta) {
        return null;
    }

    @Override
    public ItemStack getItemStack(Block block, TileEntity te, int id, int meta) {
        return null;
    }

    @Override
    public String getMod(Block block, TileEntity te, int id, int meta) {
        return null;
    }

}
