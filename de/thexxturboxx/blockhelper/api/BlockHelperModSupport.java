package de.thexxturboxx.blockhelper.api;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.item.ItemStack;

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
     * The currently registered {@link BlockHelperNameFixer}s.
     */
    private static final List<BlockHelperNameFixer> NAME_FIXERS = new ArrayList<BlockHelperNameFixer>();

    /**
     * The currently registered {@link BlockHelperItemStackFixer}s.
     */
    private static final List<BlockHelperItemStackFixer> IS_FIXERS = new ArrayList<BlockHelperItemStackFixer>();

    /**
     * The currently registered {@link BlockHelperModFixer}s.
     */
    private static final List<BlockHelperModFixer> MOD_FIXERS = new ArrayList<BlockHelperModFixer>();

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
     * Registers a {@link BlockHelperNameFixer} to BlockHelper.
     *
     * @param fixer The {@link BlockHelperNameFixer} to register.
     */
    public static void registerNameFixer(BlockHelperNameFixer fixer) {
        NAME_FIXERS.add(fixer);
    }

    /**
     * Registers a {@link BlockHelperItemStackFixer} to BlockHelper.
     *
     * @param fixer The {@link BlockHelperItemStackFixer} to register.
     */
    public static void registerItemStackFixer(BlockHelperItemStackFixer fixer) {
        IS_FIXERS.add(fixer);
    }

    /**
     * Registers a {@link BlockHelperModFixer} to BlockHelper.
     *
     * @param fixer The {@link BlockHelperModFixer} to register.
     */
    public static void registerModFixer(BlockHelperModFixer fixer) {
        MOD_FIXERS.add(fixer);
    }

    /**
     * Adds information from all currently registered {@link BlockHelperBlockProvider}s to the given {@link InfoHolder}.
     *
     * @param state The current {@link BlockHelperBlockState}.
     * @param info  The {@link InfoHolder} to add the information to.
     */
    public static void addInfo(BlockHelperBlockState state, InfoHolder info) {
        for (BlockHelperBlockProvider p : BLOCK_PROVIDERS) {
            if (p.isEnabled()) {
                try {
                    p.addInformation(state, info);
                } catch (Throwable ignored) {
                }
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
            if (p.isEnabled()) {
                try {
                    p.addInformation(state, info);
                } catch (Throwable ignored) {
                }
            }
        }
    }

    /**
     * Searches all currently registered {@link BlockHelperNameFixer}s for a correct item name.
     *
     * @param state The current {@link BlockHelperBlockState}.
     */
    public static String getName(BlockHelperBlockState state) {
        for (BlockHelperNameFixer f : NAME_FIXERS) {
            try {
                String name = f.getName(state);
                if (name != null && !name.isEmpty()) {
                    return name;
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    /**
     * Searches all currently registered {@link BlockHelperItemStackFixer}s for a correct {@link ItemStack}.
     *
     * @param state The current {@link BlockHelperBlockState}.
     */
    public static ItemStack getItemStack(BlockHelperBlockState state) {
        for (BlockHelperItemStackFixer f : IS_FIXERS) {
            try {
                ItemStack stack = f.getItemStack(state);
                if (stack != null) {
                    return stack;
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    /**
     * Searches all currently registered {@link BlockHelperModFixer}s for a correct mod name.
     *
     * @param state The current {@link BlockHelperBlockState}.
     */
    public static String getMod(BlockHelperBlockState state) {
        for (BlockHelperModFixer f : MOD_FIXERS) {
            try {
                String mod = f.getMod(state);
                if (mod != null && !mod.isEmpty()) {
                    return mod;
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    /**
     * Searches all currently registered {@link BlockHelperModFixer}s for a correct mod name.
     *
     * @param object The current object, most likely an instance of {@link net.minecraft.item.Item}.
     */
    public static String getMod(Object object) {
        for (BlockHelperModFixer f : MOD_FIXERS) {
            try {
                String mod = f.getMod(object);
                if (mod != null && !mod.isEmpty()) {
                    return mod;
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

}
