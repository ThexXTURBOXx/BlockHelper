package de.thexxturboxx.blockhelper.integration;

import codechicken.chunkloader.TileChunkLoader;
import codechicken.chunkloader.TileChunkLoaderBase;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import net.minecraft.tileentity.TileEntity;

public class CChunksIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "codechicken.chunkloader.TileChunkLoaderBase")) {
            info.add("Owner: " + ((TileChunkLoaderBase) te).getOwner());
            info.add("Active: " + firstUp(Boolean.toString(((TileChunkLoaderBase) te).active)));
            if (iof(te, "codechicken.chunkloader.TileChunkLoader")) {
                int radius = ((TileChunkLoader) te).radius;
                info.add("Radius: " + radius);
                info.add("Shape: " + ((TileChunkLoader) te).shape.toString());
            }
        }
    }

    private String firstUp(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

}
