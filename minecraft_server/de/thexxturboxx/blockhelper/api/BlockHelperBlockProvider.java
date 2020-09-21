package de.thexxturboxx.blockhelper.api;

import de.thexxturboxx.blockhelper.InfoHolder;
import net.minecraft.src.Block;

public interface BlockHelperBlockProvider {

    void addInformation(Block block, int id, int meta, InfoHolder info);

}
