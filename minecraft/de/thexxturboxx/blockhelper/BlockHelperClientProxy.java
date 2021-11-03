package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
import java.io.File;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.input.Keyboard;

public class BlockHelperClientProxy extends BlockHelperCommonProxy {

    public static double size;
    public static double sizeInv;
    public static int background;
    public static int gradient1;
    public static int gradient2;
    public static KeyBinding showHide;

    @Override
    public void load(mod_BlockHelper instance) {
        super.load(instance);
        ModLoader.SetInGameHook(instance, true, false);
        ModIdentifier.load();
        Configuration cfg = new Configuration(new File("config/BlockHelper.cfg"));
        cfg.load();
        size = Double.parseDouble(cfg.getOrCreateProperty("Size", Configuration.GENERAL_PROPERTY, "1.0").value);
        background = parseUnsignedInt(cfg.getOrCreateProperty("BackgroundColor", Configuration.GENERAL_PROPERTY,
                "cc100010").value, 16);
        gradient1 = parseUnsignedInt(cfg.getOrCreateProperty("BorderColor1", Configuration.GENERAL_PROPERTY,
                "cc5000ff").value, 16);
        gradient2 = parseUnsignedInt(cfg.getOrCreateProperty("BorderColor2", Configuration.GENERAL_PROPERTY,
                "cc28007f").value, 16);
        cfg.save();
        sizeInv = 1 / size;
        showHide = new KeyBinding("blockhelper.key_show_hide", Keyboard.KEY_NUMPAD0);
        ModLoader.RegisterKey(instance, showHide, false);
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