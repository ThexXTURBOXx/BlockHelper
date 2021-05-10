package de.thexxturboxx.blockhelper;

import java.io.File;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.input.Keyboard;

public class BlockHelperClientProxy extends BlockHelperCommonProxy {

    public static double size;
    public static double sizeInv;
    public static int mode;
    public static KeyBinding showHide;

    @Override
    public EntityPlayer getPlayer() {
        return ModLoader.getMinecraftInstance().thePlayer;
    }

    @Override
    public World getWorld() {
        return ModLoader.getMinecraftInstance().theWorld;
    }

    @Override
    public void load(mod_BlockHelper instance) {
        super.load(instance);
        mod_BlockHelper.isClient = true;
        Configuration cfg = new Configuration(new File("config/BlockHelper.cfg"));
        cfg.load();
        try {
            size = Double.parseDouble(cfg.getOrCreateProperty("General", "Size", "1.0").value);
        } catch (NumberFormatException e) {
            size = 1;
        }
        mode = cfg.getOrCreateIntProperty("Mode", "General", 0).getInt();
        cfg.save();
        sizeInv = 1 / size;
        showHide = new KeyBinding("Show/Hide Block Helper", Keyboard.KEY_NUMPAD0);
        ModLoader.registerKey(instance, showHide, false);
    }

}