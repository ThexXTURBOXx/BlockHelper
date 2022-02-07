package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.i18n.I18n;
import de.thexxturboxx.blockhelper.integration.IntegrationRegistrar;
import net.minecraft.src.mod_BlockHelper;

public class BlockHelperCommonProxy {

    public static boolean showHealth;
    public static boolean advMachinesIntegration;
    public static boolean bcIntegration;
    public static boolean forestryIntegration;
    public static boolean ic2Integration;
    public static boolean redPower2Integration;
    public static boolean vanillaIntegration;

    public void load(mod_BlockHelper instance) {
        I18n.init();
        IntegrationRegistrar.init();
        Thread versionCheckThread = new Thread(new BlockHelperUpdater(), "Block Helper Version Check");
        versionCheckThread.start();
        showHealth = parseBooleanTrueDefault(mod_BlockHelper.showHealthStr);
        advMachinesIntegration = parseBooleanTrueDefault(mod_BlockHelper.advMachinesIntegrationStr);
        bcIntegration = parseBooleanTrueDefault(mod_BlockHelper.bcIntegrationStr);
        forestryIntegration = parseBooleanTrueDefault(mod_BlockHelper.forestryIntegrationStr);
        ic2Integration = parseBooleanTrueDefault(mod_BlockHelper.ic2IntegrationStr);
        redPower2Integration = parseBooleanTrueDefault(mod_BlockHelper.redPower2IntegrationStr);
        vanillaIntegration = parseBooleanTrueDefault(mod_BlockHelper.vanillaIntegrationStr);
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
