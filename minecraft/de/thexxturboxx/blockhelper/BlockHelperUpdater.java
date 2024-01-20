package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.i18n.I18n;
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
                mod_BlockHelper.LOGGER.info(I18n.format("newer_version_available", mod_BlockHelper.NAME,
                        latestVersion));
            } else {
                mod_BlockHelper.LOGGER.info(I18n.format("newest_version_installed", mod_BlockHelper.NAME));
            }
        } catch (Throwable t) {
            t.printStackTrace();
            mod_BlockHelper.LOGGER.warning(I18n.format("update_check_failed", mod_BlockHelper.NAME));
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
        if (!isLatestVersion()) {
            if (getLatestVersion().equals(mod_BlockHelper.VERSION)) {
                mc.thePlayer.func_22055_b(I18n.format("update_check_failed_chat", mod_BlockHelper.NAME));
            } else {
                mc.thePlayer.func_22055_b(I18n.format("newer_version_available_chat", mod_BlockHelper.NAME,
                        mod_BlockHelper.VERSION, getLatestVersion()));
            }
        }
    }

}
