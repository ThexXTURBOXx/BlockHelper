package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.i18n.I18n;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import net.minecraft.server.mod_BlockHelper;

public class BlockHelperUpdater implements Runnable {

    private static final String JSON_URL = "https://raw.githubusercontent.com/"
            + "ThexXTURBOXx/UpdateJSONs/master/block-helper.csv";

    /**
     * Let the Version Checker run
     */
    @Override
    public void run() {
        try {
            // Fix older versions of Java
            System.setProperty("https.protocols", "TLSv1,TLSv1.1,TLSv1.2");
            String latestVersion = getLatestModVersion(new URL(JSON_URL).openStream());
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
    }

    private static String getLatestModVersion(InputStream is) throws IOException {
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        try {
            String line = br.readLine();
            while (line != null) {
                String[] split = line.split(",", 2);
                if (mod_BlockHelper.MC_VERSION.equals(split[0])) {
                    return split[1];
                }
                line = br.readLine();
            }
            throw new IllegalArgumentException("Version not found.");
        } finally {
            is.close();
            isr.close();
            br.close();
        }
    }

}
