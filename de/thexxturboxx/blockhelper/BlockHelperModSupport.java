package de.thexxturboxx.blockhelper;

import java.util.ArrayList;
import java.util.List;

class BlockHelperModSupport {

    static {
        loadedCache = new ArrayList<String>();
        loadedCacheFailed = new ArrayList<String>();
    }

    private static final List<String> loadedCache;
    private static final List<String> loadedCacheFailed;

    static boolean isLoadedAndInstanceOf(Object obj, String clazz) {
        if (loadedCacheFailed.contains(clazz))
            return false;
        try {
            Class c = Class.forName(clazz);
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

}