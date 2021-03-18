package de.thexxturboxx.blockhelper.api;

import de.thexxturboxx.blockhelper.integration.BlockHelperNameFixer;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.src.Block;
import net.minecraft.src.TileEntity;

public final class BlockHelperModSupport {

    private static final List<BlockHelperBlockProvider> BLOCK_PROVIDERS = new ArrayList<BlockHelperBlockProvider>();
    private static final List<BlockHelperTileEntityProvider> TE_PROVIDERS =
            new ArrayList<BlockHelperTileEntityProvider>();
    private static final List<BlockHelperNameFixer> NAME_FIXERS = new ArrayList<BlockHelperNameFixer>();

    private BlockHelperModSupport() {
        throw new UnsupportedOperationException();
    }

    public static void registerInfoProvider(BlockHelperInfoProvider provider) {
        registerBlockProvider(provider);
        registerTileEntityProvider(provider);
        registerNameFixer(provider);
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

}
