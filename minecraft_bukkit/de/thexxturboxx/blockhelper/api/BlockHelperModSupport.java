package de.thexxturboxx.blockhelper.api;

import java.util.ArrayList;
import java.util.List;

/**
 * Adds functionality for registering information providers to BlockHelper.
 */
public final class BlockHelperModSupport {

    /**
     * The currently registered {@link BlockHelperBlockProvider}s.
     */
    private static final List<BlockHelperBlockProvider> BLOCK_PROVIDERS = new ArrayList<BlockHelperBlockProvider>();

    /**
     * The currently registered {@link BlockHelperEntityProvider}s.
     */
    private static final List<BlockHelperEntityProvider> ENTITY_PROVIDERS = new ArrayList<BlockHelperEntityProvider>();

    /**
     * Don't initialize me.
     */
    private BlockHelperModSupport() {
        throw new UnsupportedOperationException();
    }

    /**
     * Registers a {@link BlockHelperBlockProvider} to BlockHelper.
     *
     * @param provider The {@link BlockHelperBlockProvider} to register.
     */
    public static void registerBlockProvider(BlockHelperBlockProvider provider) {
        BLOCK_PROVIDERS.add(provider);
    }

    /**
     * Registers a {@link BlockHelperEntityProvider} to BlockHelper.
     *
     * @param provider The {@link BlockHelperEntityProvider} to register.
     */
    public static void registerEntityProvider(BlockHelperEntityProvider provider) {
        ENTITY_PROVIDERS.add(provider);
    }

    /**
     * Adds information from all currently registered {@link BlockHelperBlockProvider}s to the given {@link InfoHolder}.
     *
     * @param state The current {@link BlockHelperBlockState}.
     * @param info  The {@link InfoHolder} to add the information to.
     */
    public static void addInfo(BlockHelperBlockState state, InfoHolder info) {
        for (BlockHelperBlockProvider p : BLOCK_PROVIDERS) {
            try {
                p.addInformation(state, info);
            } catch (Throwable ignored) {
            }
        }
    }

    /**
     * Adds information from all currently registered {@link BlockHelperEntityProvider}s to the given
     * {@link InfoHolder}.
     *
     * @param state The current {@link BlockHelperEntityState}.
     * @param info  The {@link InfoHolder} to add the information to.
     */
    public static void addInfo(BlockHelperEntityState state, InfoHolder info) {
        for (BlockHelperEntityProvider p : ENTITY_PROVIDERS) {
            try {
                p.addInformation(state, info);
            } catch (Throwable ignored) {
            }
        }
    }

}
