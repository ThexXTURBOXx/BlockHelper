package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.InfoHolder;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.src.Block;
import net.minecraft.src.MathHelper;

public class FSIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(Block b, int id, int meta, InfoHolder info) {
        if (iof(b, "flora.BerryBush")) {
            int newMeta = MathHelper.floor_double(meta / 4d);
            if (newMeta < 3) {
                String grow = ((int) ((newMeta / 2d) * 100)) + "";
                if (grow.equals("100")) {
                    grow = "Mature";
                } else {
                    grow = grow + "%";
                }
                info.add("Growth State: " + grow);
            } else {
                info.add("Growth State: Ripe");
            }
        }
    }

}
