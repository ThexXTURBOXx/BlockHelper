package de.thexxturboxx.blockhelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class BlockHelperUpdater implements Runnable {

    private static final String JSON_URL = "https://raw.githubusercontent.com/"
            + "ThexXTURBOXx/UpdateJSONs/master/blockhelper.json";
    private static String latestVersion = "";

    /**
     * Let the Version Checker run
     */
    @Override
    public void run() {
        InputStream in = null;
        try {
            in = new URL(JSON_URL).openStream();
        } catch (IOException e) {
            e.printStackTrace();
            mod_BlockHelper.LOGGER.warning("Update check for " + mod_BlockHelper.NAME + " failed.");
        }
        if (in != null) {
            try {
                for (String s : readLines(in)) {
                    if (s.contains("modVersion")) {
                        s = s.substring(0, s.indexOf("|"));
                        s = s.replace("modVersion", "");
                        s = s.replace("\"", "");
                        s = s.replace(",", "");
                        s = s.replace("	", "");
                        s = s.replace(":", "");
                        latestVersion = s;
                    }
                }
                if (!latestVersion.equalsIgnoreCase(mod_BlockHelper.VERSION)) {
                    mod_BlockHelper.LOGGER.info("Newer version of " + mod_BlockHelper.NAME + " available: " + latestVersion);
                } else {
                    mod_BlockHelper.LOGGER.info("Yay! You have the newest version of " + mod_BlockHelper.NAME + " :)");
                }
            } catch (IOException e) {
                e.printStackTrace();
                mod_BlockHelper.LOGGER.warning("Update check for " + mod_BlockHelper.NAME + " failed.");
            }
        }
    }

    private static List<String> readLines(InputStream input) throws IOException {
        InputStreamReader reader = new InputStreamReader(input);
        BufferedReader readers = new BufferedReader(reader);
        List<String> list = new ArrayList<String>();
        String line = readers.readLine();
        while (line != null) {
            list.add(line);
            line = readers.readLine();
        }
        return list;
    }

}
