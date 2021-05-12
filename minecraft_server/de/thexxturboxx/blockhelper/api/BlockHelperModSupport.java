package de.thexxturboxx.blockhelper.api;

import java.util.ArrayList;
import java.util.List;

public final class BlockHelperModSupport {

    private static final List<BlockHelperBlockProvider> BLOCK_PROVIDERS = new ArrayList<BlockHelperBlockProvider>();

    private BlockHelperModSupport() {
        throw new UnsupportedOperationException();
    }

    public static void registerBlockProvider(BlockHelperBlockProvider provider) {
        BLOCK_PROVIDERS.add(provider);
    }

    public static void addInfo(BlockHelperState state, InfoHolder info) {
        for (BlockHelperBlockProvider p : BLOCK_PROVIDERS) {
            try {
                p.addInformation(state, info);
            } catch (Throwable ignored) {
            }
        }
    }

}
