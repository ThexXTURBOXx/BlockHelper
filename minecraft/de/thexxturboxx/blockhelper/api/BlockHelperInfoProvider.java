package de.thexxturboxx.blockhelper.api;

import de.thexxturboxx.blockhelper.InfoHolder;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;

public class BlockHelperInfoProvider implements BlockHelperBlockProvider, BlockHelperTileEntityProvider {

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

    @Override
    public void addInformation(Block block, int id, int meta, InfoHolder info) {
    }

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
    }

}
