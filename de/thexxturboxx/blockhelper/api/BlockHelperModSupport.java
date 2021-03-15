package de.thexxturboxx.blockhelper.api;

import de.thexxturboxx.blockhelper.InfoHolder;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class BlockHelperModSupport {

    private static final List<BlockHelperBlockProvider> BLOCK_PROVIDERS = new ArrayList<BlockHelperBlockProvider>();
    private static final List<BlockHelperTileEntityProvider> TE_PROVIDERS =
            new ArrayList<BlockHelperTileEntityProvider>();

    public static void registerInfoProvider(BlockHelperInfoProvider provider) {
        registerBlockProvider(provider);
        registerTileEntityProvider(provider);
    }

    public static void registerBlockProvider(BlockHelperBlockProvider provider) {
        BLOCK_PROVIDERS.add(provider);
    }

    public static void registerTileEntityProvider(BlockHelperTileEntityProvider provider) {
        TE_PROVIDERS.add(provider);
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
