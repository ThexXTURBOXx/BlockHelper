package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import net.minecraft.server.Block;

public class RedPower2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(Block block, int id, int meta, InfoHolder info) {
        if (iof(block, "eloraam.world.BlockCustomCrops")) {
            if (meta < 5) {
                int grow = (int) ((meta / 4d) * 100);
                String toShow;
                if (grow >= 100) {
                    toShow = "Mature";
                } else {
                    toShow = grow + "%";
                }
                info.add("Growth State: " + toShow);
            } else {
                info.add("Growth State: Ripe");
            }
        }
    }

}
