package de.thexxturboxx.blockhelper;

import java.io.File;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.World;
import net.minecraft.src.forge.Configuration;

public class BlockHelperClientProxy extends BlockHelperCommonProxy {

    // static double size;
    static int mode;

    @Override
    public EntityPlayer getPlayer() {
        return null;
    }

    @Override
    public World getWorld() {
        return null;
    }

    @Override
    public void load(mod_BlockHelper instance) {
        super.load(instance);
        mod_BlockHelper.isClient = true;
        Configuration cfg = new Configuration(new File("config/BlockHelper.cfg"));
        cfg.load();
        // size = cfg.get("General", "Size", 1D, "Size factor for the tooltip").getDouble(1);
        mode = cfg.getOrCreateIntProperty("Mode", "General", 0).getInt();
        cfg.save();
    }

}