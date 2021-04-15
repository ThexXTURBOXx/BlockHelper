package de.thexxturboxx.blockhelper.api;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public final class BlockHelperModSupport {

    private static final List<BlockHelperBlockProvider> BLOCK_PROVIDERS = new ArrayList<BlockHelperBlockProvider>();
    private static final List<BlockHelperTileEntityProvider> TE_PROVIDERS =
            new ArrayList<BlockHelperTileEntityProvider>();
    private static final List<BlockHelperNameFixer> NAME_FIXERS = new ArrayList<BlockHelperNameFixer>();
    private static final List<BlockHelperItemStackFixer> IS_FIXERS = new ArrayList<BlockHelperItemStackFixer>();
    private static final List<BlockHelperModFixer> MOD_FIXERS = new ArrayList<BlockHelperModFixer>();

    private BlockHelperModSupport() {
        throw new UnsupportedOperationException();
    }

    public static void registerBlockProvider(BlockHelperBlockProvider provider) {
        BLOCK_PROVIDERS.add(provider);
    }

    public static void registerTileEntityProvider(BlockHelperTileEntityProvider provider) {
        TE_PROVIDERS.add(provider);
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

    public static String getName(Block block, TileEntity te, int id, int meta) {
        for (BlockHelperNameFixer f : NAME_FIXERS) {
            try {
                String name = f.getName(block, te, id, meta);
                if (name != null && !name.isEmpty()) {
                    return name;
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    public static void addInfo(InfoHolder info, Block block, int id, int meta, TileEntity te) {
        for (BlockHelperBlockProvider p : BLOCK_PROVIDERS) {
            try {
                p.addInformation(block, id, meta, info);
            } catch (Throwable ignored) {
            }
        }
        for (BlockHelperTileEntityProvider p : TE_PROVIDERS) {
            try {
                p.addInformation(te, id, meta, info);
            } catch (Throwable ignored) {
            }
        }
    }

    public static ItemStack getItemStack(Block block, TileEntity te, int id, int meta) {
        for (BlockHelperItemStackFixer f : IS_FIXERS) {
            try {
                ItemStack stack = f.getItemStack(block, te, id, meta);
                if (stack != null) {
                    return stack;
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    public static String getMod(Block block, TileEntity te, int id, int meta) {
        for (BlockHelperModFixer f : MOD_FIXERS) {
            try {
                String mod = f.getMod(block, te, id, meta);
                if (mod != null && !mod.isEmpty()) {
                    return mod;
                }
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

}
