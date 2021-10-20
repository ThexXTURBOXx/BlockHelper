package de.thexxturboxx.blockhelper;

import cpw.mods.fml.relauncher.FMLInjectionData;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.Configuration;
import org.lwjgl.input.Keyboard;

public class BlockHelperClientProxy extends BlockHelperCommonProxy {

    public static double size;
    public static double sizeInv;
    public static int background;
    public static int gradient1;
    public static int gradient2;
    public static int font;
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
        size = cfg.get("General", "Size", 1D, "Size factor for the tooltip").getDouble(1);
        background = parseUnsignedInt(cfg.get("General", "BackgroundColor",
                "cc100010", "Background Color Hex Code").getString(), 16);
        gradient1 = parseUnsignedInt(cfg.get("General", "BorderColor1",
                "cc5000ff", "Border Color Hex Code 1").getString(), 16);
        gradient2 = parseUnsignedInt(cfg.get("General", "BorderColor2",
                "cc28007f", "Border Color Hex Code 2").getString(), 16);
        cfg.save();
        sizeInv = 1 / size;
        showHide = new KeyBinding("Show/Hide Block Helper", Keyboard.KEY_NUMPAD0);
        ModLoader.registerKey(instance, showHide, false);
    }

    /**
     * This method is copied from JDK 8, because it isn't available in JDK 7 or less.
     *
     * @param s     The string to parse.
     * @param radix The radix to parse with.
     * @return The parsed unsigned integer.
     * @throws NumberFormatException Some parsing error occurred.
     */
    public static int parseUnsignedInt(String s, int radix) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }

        int len = s.length();
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar == '-') {
                throw new NumberFormatException(String.format("Illegal leading minus sign "
                        + "on unsigned string %s.", s));
            } else {
                if (len <= 5 || (radix == 10 && len <= 9)) {
                    return Integer.parseInt(s, radix);
                } else {
                    long ell = Long.parseLong(s, radix);
                    if ((ell & 0xffffffff00000000L) == 0) {
                        return (int) ell;
                    } else {
                        throw new NumberFormatException(String.format("String value %s exceeds "
                                + "range of unsigned int.", s));
                    }
                }
            }
        } else {
            throw new NumberFormatException("For input string: \"" + s + "\"");
        }
    }

}
