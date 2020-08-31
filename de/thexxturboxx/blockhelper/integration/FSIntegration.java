package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.block.Block;
import net.minecraft.util.MathHelper;

public class FSIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(Block b, int id, int meta, InfoHolder info) {
        if (iof(b, "florasoma.crops.blocks.BerryBush")) {
            int newMeta = MathHelper.floor_double(meta / 4d);
            if (newMeta < 3) {
                String grow = ((int) ((newMeta / 2d) * 100)) + "";
                if (grow.equals("100")) {
                    grow = "Mature";
                } else {
                    grow = grow + "%";
                }
                info.add(2, "Growth State: " + grow);
            } else {
                info.add(2, "Growth State: Ripe");
            }
        }
    }

}
