package de.thexxturboxx.blockhelper;

import cpw.mods.fml.relauncher.FMLInjectionData;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;
import net.minecraft.src.mod_BlockHelper;
import net.minecraftforge.common.Configuration;
import org.lwjgl.input.Keyboard;

public class BlockHelperClientProxy extends BlockHelperCommonProxy {

    public static double size;
    public static double sizeInv;
    public static int mode;
    public static KeyBinding showHide;

    @Override
    public EntityPlayer getPlayer() {
        return Minecraft.getMinecraft().thePlayer;
    }

    @Override
    public World getWorld() {
        return Minecraft.getMinecraft().theWorld;
    }

    @Override
    public void load(mod_BlockHelper instance) {
        super.load(instance);
        mod_BlockHelper.isClient = true;
        Configuration cfg = new Configuration(new File((File) FMLInjectionData.data()[6], "config/BlockHelper.cfg"));
        cfg.load();
        try {
            size = Double.parseDouble(cfg.get("General", "Size", "1.0").value);
        } catch (NumberFormatException e) {
            size = 1;
        }
        mode = cfg.get("General", "Mode", 0).getInt(0);
        cfg.save();
        sizeInv = 1 / size;
        showHide = new KeyBinding("Show/Hide Block Helper", Keyboard.KEY_NUMPAD0);
        ModLoader.registerKey(instance, showHide, false);
    }

}