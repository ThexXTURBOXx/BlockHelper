package de.thexxturboxx.blockhelper.api;

import net.minecraft.server.Block;

public interface BlockHelperBlockProvider {

    void addInformation(Block block, int id, int meta, InfoHolder info);

}
