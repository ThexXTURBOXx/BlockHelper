package de.thexxturboxx.blockhelper;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.FMLInjectionData;
import de.thexxturboxx.blockhelper.i18n.I18n;
import de.thexxturboxx.blockhelper.integration.IntegrationRegistrar;
import java.io.File;
import net.minecraft.src.mod_BlockHelper;
import net.minecraftforge.common.Configuration;

public class BlockHelperCommonProxy {

    protected static Configuration cfg;

    public static boolean showHealth;
    public static boolean advMachinesIntegration;
    public static boolean bcIntegration;
    public static boolean factorizationIntegration;
    public static boolean gregTechIntegration;
    public static boolean ic2Integration;
    public static boolean neiIntegration;
    public static boolean teIntegration;
    public static boolean vanillaIntegration;

    public void load(mod_BlockHelper instance) {
        mod_BlockHelper.isClient = false;
        I18n.init();
        NetworkRegistry.instance().registerChannel(instance, mod_BlockHelper.CHANNEL);
        IntegrationRegistrar.init();
        Thread versionCheckThread = new Thread(new BlockHelperUpdater(), "Block Helper Version Check");
        versionCheckThread.start();
        cfg = new Configuration(new File((File) FMLInjectionData.data()[6], "config/BlockHelper.cfg"));
        cfg.load();
        showHealth = cfg.get("General", "ShowHealth", true).getBoolean(true);
        advMachinesIntegration = cfg.get("General", "AdvMachinesIntegration", true).getBoolean(true);
        bcIntegration = cfg.get("General", "BuildCraftIntegration", true).getBoolean(true);
        factorizationIntegration = cfg.get("General", "FactorizationIntegration", true).getBoolean(true);
        gregTechIntegration = cfg.get("General", "GregTechIntegration", true).getBoolean(true);
        ic2Integration = cfg.get("General", "Ic2Integration", true).getBoolean(true);
        neiIntegration = cfg.get("General", "NEIIntegration", true).getBoolean(true);
        teIntegration = cfg.get("General", "ThermalExpansionIntegration", true).getBoolean(true);
        vanillaIntegration = cfg.get("General", "VanillaIntegration", true).getBoolean(true);
        cfg.save();
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
