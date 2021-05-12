package de.thexxturboxx.blockhelper.api;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.ItemStack;

public final class BlockHelperModSupport {

    private static final List<BlockHelperBlockProvider> BLOCK_PROVIDERS = new ArrayList<BlockHelperBlockProvider>();
    private static final List<BlockHelperNameFixer> NAME_FIXERS = new ArrayList<BlockHelperNameFixer>();
    private static final List<BlockHelperItemStackFixer> IS_FIXERS = new ArrayList<BlockHelperItemStackFixer>();
    private static final List<BlockHelperModFixer> MOD_FIXERS = new ArrayList<BlockHelperModFixer>();

    private BlockHelperModSupport() {
        throw new UnsupportedOperationException();
    }

    public static void registerBlockProvider(BlockHelperBlockProvider provider) {
        BLOCK_PROVIDERS.add(provider);
    }

    public static void registerNameFixer(BlockHelperNameFixer provider) {
        NAME_FIXERS.add(provider);
    }

    public static void registerItemStackFixer(BlockHelperItemStackFixer provider) {
        IS_FIXERS.add(provider);
    }

    public static void registerModFixer(BlockHelperModFixer provider) {
        MOD_FIXERS.add(provider);
    }

    public static void addInfo(BlockHelperState state, InfoHolder info) {
        for (BlockHelperBlockProvider p : BLOCK_PROVIDERS) {
            try {
                p.addInformation(state, info);
            } catch (Throwable ignored) {
            }
        }
    }

    public static String getName(BlockHelperState state) {
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

    public static ItemStack getItemStack(BlockHelperState state) {
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

    public static String getMod(BlockHelperState state) {
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

}
