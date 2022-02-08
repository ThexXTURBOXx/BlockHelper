package de.thexxturboxx.blockhelper;

import cpw.mods.fml.common.FMLCommonHandler;
import de.thexxturboxx.blockhelper.i18n.I18n;
import de.thexxturboxx.blockhelper.integration.IntegrationRegistrar;
import java.io.File;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.Configuration;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.mod_BlockHelper;

public class BlockHelperCommonProxy {

    protected static Configuration cfg;

    public static boolean showHealth;
    public static boolean advMachinesIntegration;
    public static boolean bcIntegration;
    public static boolean floraSomaIntegration;
    public static boolean forestryIntegration;
    public static boolean gregTechIntegration;
    public static boolean ic2Integration;
    public static boolean neiIntegration;
    public static boolean pamIntegration;
    public static boolean redPower2Integration;
    public static boolean teIntegration;
    public static boolean vanillaIntegration;

    public void load(mod_BlockHelper instance) {
        I18n.init();
        MinecraftForge.registerConnectionHandler(instance);
        ModLoader.registerPacketChannel(instance, mod_BlockHelper.CHANNEL);
        IntegrationRegistrar.init();
        Thread versionCheckThread = new Thread(new BlockHelperUpdater(), "Block Helper Version Check");
        versionCheckThread.start();
        cfg = new Configuration(new File(FMLCommonHandler.instance().getMinecraftRootDirectory(), "config/BlockHelper.cfg"));
        cfg.load();
        showHealth = parseBooleanTrueDefault(cfg.getOrCreateProperty("ShowHealth", "General", "true").value);
        advMachinesIntegration = parseBooleanTrueDefault(cfg.getOrCreateProperty("AdvMachinesIntegration", "General", "true").value);
        bcIntegration = parseBooleanTrueDefault(cfg.getOrCreateProperty("BuildCraftIntegration", "General", "true").value);
        floraSomaIntegration = parseBooleanTrueDefault(cfg.getOrCreateProperty("FloraSomaIntegration", "General", "true").value);
        forestryIntegration = parseBooleanTrueDefault(cfg.getOrCreateProperty("ForestryIntegration", "General", "true").value);
        gregTechIntegration = parseBooleanTrueDefault(cfg.getOrCreateProperty("GregTechIntegration", "General", "true").value);
        ic2Integration = parseBooleanTrueDefault(cfg.getOrCreateProperty("Ic2Integration", "General", "true").value);
        neiIntegration = parseBooleanTrueDefault(cfg.getOrCreateProperty("NEIIntegration", "General", "true").value);
        pamIntegration = parseBooleanTrueDefault(cfg.getOrCreateProperty("PamIntegration", "General", "true").value);
        redPower2Integration = parseBooleanTrueDefault(cfg.getOrCreateProperty("RedPower2Integration", "General", "true").value);
        teIntegration = parseBooleanTrueDefault(cfg.getOrCreateProperty("ThermalExpansionIntegration", "General", "true").value);
        vanillaIntegration = parseBooleanTrueDefault(cfg.getOrCreateProperty("VanillaIntegration", "General", "true").value);
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
