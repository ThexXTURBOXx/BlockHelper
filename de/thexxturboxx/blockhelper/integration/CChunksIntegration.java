package de.thexxturboxx.blockhelper.integration;

import codechicken.chunkloader.TileChunkLoader;
import codechicken.chunkloader.TileChunkLoaderBase;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperState;
import de.thexxturboxx.blockhelper.api.InfoHolder;

public class CChunksIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperState state, InfoHolder info) {
        if (iof(state.te, "codechicken.chunkloader.TileChunkLoaderBase")) {
            info.add("Owner: " + ((TileChunkLoaderBase) state.te).getOwner());
            info.add("Active: " + firstUp(Boolean.toString(((TileChunkLoaderBase) state.te).active)));
            if (iof(state.te, "codechicken.chunkloader.TileChunkLoader")) {
                int radius = ((TileChunkLoader) state.te).radius;
                info.add("Radius: " + radius);
                info.add("Shape: " + ((TileChunkLoader) state.te).shape.toString());
            }
        }
    }

    @Override
    public String getMod(BlockHelperState state) {
        if (iof(state.te, "codechicken.chunkloader.TileChunkLoaderBase")) {
            return "ChickenChunks";
        }
        return super.getMod(state);
    }

    private String firstUp(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

}
