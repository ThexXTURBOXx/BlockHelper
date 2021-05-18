package de.thexxturboxx.blockhelper.api;

import java.util.ArrayList;
import java.util.List;

public final class BlockHelperModSupport {

    private static final List<BlockHelperBlockProvider> BLOCK_PROVIDERS = new ArrayList<BlockHelperBlockProvider>();
    private static final List<BlockHelperEntityProvider> ENTITY_PROVIDERS = new ArrayList<BlockHelperEntityProvider>();

    private BlockHelperModSupport() {
        throw new UnsupportedOperationException();
    }

    public static void registerBlockProvider(BlockHelperBlockProvider provider) {
        BLOCK_PROVIDERS.add(provider);
    }

    public static void registerEntityProvider(BlockHelperEntityProvider provider) {
        ENTITY_PROVIDERS.add(provider);
    }

    public static void addInfo(BlockHelperBlockState state, InfoHolder info) {
        for (BlockHelperBlockProvider p : BLOCK_PROVIDERS) {
            try {
                p.addInformation(state, info);
            } catch (Throwable ignored) {
            }
        }
    }

    public static void addInfo(BlockHelperEntityState state, InfoHolder info) {
        for (BlockHelperEntityProvider p : ENTITY_PROVIDERS) {
            try {
                p.addInformation(state, info);
            } catch (Throwable ignored) {
            }
        }
    }

}
