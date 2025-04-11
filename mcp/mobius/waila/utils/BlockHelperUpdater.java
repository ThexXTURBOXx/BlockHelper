package mcp.mobius.waila.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.logging.Level;
import mcp.mobius.waila.cbcore.LangUtil;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.client.Minecraft;

public class BlockHelperUpdater implements Runnable {

    private static final String JSON_URL = "https://raw.githubusercontent.com/"
                                           + "ThexXTURBOXx/UpdateJSONs/master/block-helper.csv";

    public boolean notify = false;
    private boolean isLatestVersion = true;
    private String latestVersion = "";

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
                mod_BlockHelper.LOG.info(LangUtil.translateG("waila.newer_version_available",
                        mod_BlockHelper.NAME, latestVersion));
            } else {
                mod_BlockHelper.LOG.info(LangUtil.translateG("waila.newest_version_installed",
                        mod_BlockHelper.NAME));
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, LangUtil.translateG("waila.update_check_failed",
                    mod_BlockHelper.NAME), t);
        }
        isLatestVersion = mod_BlockHelper.VERSION.equals(latestVersion);
    }

    /**
     * @return whether BlockHelper is up-to-date or not
     */
    public boolean isLatestVersion() {
        return isLatestVersion;
    }

    /**
     * @return the latest version available or the current installed version
     */
    public String getLatestVersion() {
        if (latestVersion.isEmpty()) {
            latestVersion = mod_BlockHelper.VERSION;
        }
        return latestVersion;
    }

    private String getLatestModVersion(InputStream is) throws IOException {
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

    public void notifyUpdater(Minecraft mc) {
        if (!notify) return;
        if (!isLatestVersion()) {
            if (getLatestVersion().equals(mod_BlockHelper.VERSION)) {
                mc.thePlayer.addChatMessage(LangUtil.translateG("waila.update_check_failed_chat",
                        mod_BlockHelper.NAME));
            } else {
                mc.thePlayer.addChatMessage(LangUtil.translateG("waila.newer_version_available_chat",
                        mod_BlockHelper.NAME, mod_BlockHelper.VERSION, getLatestVersion()));
            }
        }
    }

}
