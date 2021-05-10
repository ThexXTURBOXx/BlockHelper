package de.thexxturboxx.blockhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import net.minecraft.src.mod_BlockHelper;

public class BlockHelperUpdater implements Runnable {

    private static final String JSON_URL = "https://raw.githubusercontent.com/"
            + "ThexXTURBOXx/UpdateJSONs/master/block-helper.csv";

    /**
     * Let the Version Checker run
     */
    @Override
    public void run() {
        try {
            String latestVersion = getLatestModVersion(new URL(JSON_URL).openStream());
            if (!mod_BlockHelper.VERSION.equals(latestVersion)) {
                mod_BlockHelper.LOGGER.info("Newer version of " + mod_BlockHelper.NAME + " available: " + latestVersion);
            } else {
                mod_BlockHelper.LOGGER.info("Yay! You have the newest version of " + mod_BlockHelper.NAME + " :)");
            }
        } catch (Throwable t) {
            t.printStackTrace();
            mod_BlockHelper.LOGGER.warning("Update check for " + mod_BlockHelper.NAME + " failed.");
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
