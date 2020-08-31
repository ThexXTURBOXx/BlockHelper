package de.thexxturboxx.blockhelper.integration;

import codechicken.chunkloader.TileChunkLoader;
import codechicken.chunkloader.TileChunkLoaderBase;
import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.tileentity.TileEntity;

public class CChunksIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "codechicken.chunkloader.TileChunkLoaderBase")) {
            info.add(0, "Owner: " + ((TileChunkLoaderBase) te).getOwner());
            info.add(1, "Active: " + firstUp(Boolean.toString(((TileChunkLoaderBase) te).active)));
            if (iof(te, "codechicken.chunkloader.TileChunkLoader")) {
                int radius = ((TileChunkLoader) te).radius;
                info.add(2, "Radius: " + radius);
                info.add(3, "Shape: " + ((TileChunkLoader) te).shape.toString());
            }
        }
    }

    private String firstUp(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

}
