package de.thexxturboxx.blockhelper.api;

import de.thexxturboxx.blockhelper.InfoHolder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.Block;
import net.minecraft.server.TileEntity;

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

    @Override
    public void addInformation(Block block, int id, int meta, InfoHolder info) {
    }

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
    }

}
