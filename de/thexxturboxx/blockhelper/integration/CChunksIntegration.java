package de.thexxturboxx.blockhelper.integration;

import codechicken.chunkloader.TileChunkLoader;
import codechicken.chunkloader.TileChunkLoaderBase;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;

public class CChunksIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "codechicken.chunkloader.TileChunkLoaderBase")) {
            info.add(I18n.format("owner_format", ((TileChunkLoaderBase) state.te).getOwner()));
            info.add(I18n.format("active_format", I18n.format(((TileChunkLoaderBase) state.te).active)));
            if (iof(state.te, "codechicken.chunkloader.TileChunkLoader")) {
                info.add(I18n.format("radius_format", ((TileChunkLoader) state.te).radius));
                info.add(I18n.format("shape_format", ((TileChunkLoader) state.te).shape.toString()));
            }
        }
    }

    @Override
    public String getMod(BlockHelperBlockState state) {
        if (iof(state.te, "codechicken.chunkloader.TileChunkLoaderBase")) {
            return "ChickenChunks";
        }
        return super.getMod(state);
    }

}
