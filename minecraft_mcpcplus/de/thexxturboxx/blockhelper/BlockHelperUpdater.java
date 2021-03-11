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
    private static boolean isLatestVersion = true;
    private static String latestVersion = "";
    private static boolean firstTickUpdater = true;

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
            System.out.println("Update check for " + mod_BlockHelper.NAME + " failed.");
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
                    System.out.println("Newer version of " + mod_BlockHelper.NAME + " available: " + latestVersion);
                } else {
                    System.out.println("Yay! You have the newest version of " + mod_BlockHelper.NAME + " :)");
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("Update check for " + mod_BlockHelper.NAME + " failed.");
            }
        }
        isLatestVersion = mod_BlockHelper.VERSION.equals(latestVersion);
    }

    /**
     * @return whether BlockHelper is up-to-date or not
     */
    static boolean isLatestVersion() {
        return isLatestVersion;
    }

    /**
     * @return the latest version available or the current installed version
     */
    static String getLatestVersion() {
        if (latestVersion.equals("")) {
            latestVersion = mod_BlockHelper.VERSION;
        }
        return latestVersion;
    }

    /**
     * @return the latest version available or an empty string
     */
    static String getLatestVersionOrEmpty() {
        return latestVersion;
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

    static void notifyUpdater() {
        if (firstTickUpdater) {
            if (!BlockHelperUpdater.isLatestVersion()) {
                if (BlockHelperUpdater.getLatestVersion().equals(mod_BlockHelper.VERSION)) {
                    System.out.println("[" + mod_BlockHelper.NAME + "] Update Check failed.");
                } else {
                    System.out.println("[" + mod_BlockHelper.NAME + "] New version available: "
                            + mod_BlockHelper.VERSION + " ==> " + BlockHelperUpdater.getLatestVersion());
                }
                firstTickUpdater = false;
            } else if (!BlockHelperUpdater.getLatestVersionOrEmpty().equals("")) {
                firstTickUpdater = false;
            }
        }
    }

}