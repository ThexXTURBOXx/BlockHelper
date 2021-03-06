package de.thexxturboxx.blockhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import net.minecraft.client.Minecraft;
import net.minecraft.src.mod_BlockHelper;

public class BlockHelperUpdater implements Runnable {

    private static final String JSON_URL = "https://raw.githubusercontent.com/"
            + "ThexXTURBOXx/UpdateJSONs/master/block-helper.csv";
    private static boolean isLatestVersion = true;
    private static String latestVersion = "";

    /**
     * Let the Version Checker run
     */
    @Override
    public void run() {
        try {
            // Fix older versions of Java
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            latestVersion = getLatestModVersion(new URL(JSON_URL).openStream());
            if (!mod_BlockHelper.VERSION.equals(latestVersion)) {
                mod_BlockHelper.LOGGER.info("Newer version of " + mod_BlockHelper.NAME + " available: " + latestVersion);
            } else {
                mod_BlockHelper.LOGGER.info("Yay! You have the newest version of " + mod_BlockHelper.NAME + " :)");
            }
        } catch (Throwable t) {
            t.printStackTrace();
            mod_BlockHelper.LOGGER.warning("Update check for " + mod_BlockHelper.NAME + " failed.");
        }
        isLatestVersion = mod_BlockHelper.VERSION.equals(latestVersion);
    }

    /**
     * @return whether BlockHelper is up-to-date or not
     */
    public static boolean isLatestVersion() {
        return isLatestVersion;
    }

    /**
     * @return the latest version available or the current installed version
     */
    public static String getLatestVersion() {
        if (latestVersion.isEmpty()) {
            latestVersion = mod_BlockHelper.VERSION;
        }
        return latestVersion;
    }

    private static String getLatestModVersion(InputStream is) throws IOException {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        try {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",", 2);
                if (mod_BlockHelper.MC_VERSION.equals(split[0])) {
                    return split[1];
                }
            }
            throw new IllegalArgumentException("Version not found.");
        } finally {
            is.close();
            isr.close();
            br.close();
        }
    }

    public static void notifyUpdater(Minecraft mc) {
        if (!BlockHelperUpdater.isLatestVersion()) {
            if (BlockHelperUpdater.getLatestVersion().equals(mod_BlockHelper.VERSION)) {
                mc.thePlayer.addChatMessage("§7[§6" + mod_BlockHelper.NAME + "§7] §4Update Check failed.");
            } else {
                mc.thePlayer.addChatMessage("§7[§6" + mod_BlockHelper.NAME + "§7] §bNew version available: §c"
                        + mod_BlockHelper.VERSION + " §6==> §2" + BlockHelperUpdater.getLatestVersion());
            }
        }
    }

}
