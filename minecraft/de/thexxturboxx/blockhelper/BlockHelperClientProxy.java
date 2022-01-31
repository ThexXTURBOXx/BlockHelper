package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.integration.nei.ModIdentifier;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.input.Keyboard;

public class BlockHelperClientProxy extends BlockHelperCommonProxy {

    public static double size;
    public static double sizeInv;
    public static int background;
    public static int gradient1;
    public static int gradient2;
    public static boolean fixerNotify;
    public static BlockHelperKeyBinding showHide;

    @Override
    public void load(mod_BlockHelper instance) {
        super.load(instance);
        ModLoader.SetInGameHook(instance, true, false);
        ModIdentifier.load();
        size = Double.parseDouble(mod_BlockHelper.sizeStr);
        background = parseUnsignedInt(mod_BlockHelper.backgroundStr, 16);
        gradient1 = parseUnsignedInt(mod_BlockHelper.gradient1Str, 16);
        gradient2 = parseUnsignedInt(mod_BlockHelper.gradient2Str, 16);
        fixerNotify = parseBooleanTrueDefault(mod_BlockHelper.fixerNotifyStr);
        sizeInv = 1 / size;
        showHide = new BlockHelperKeyBinding("blockhelper.key_show_hide", Keyboard.KEY_NUMPAD0);
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

    public static boolean parseBooleanTrueDefault(String val) {
        return !("false".equalsIgnoreCase(val) || "0".equals(val));
    }

}
