package mcp.mobius.waila.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.client.Minecraft;

public class BlockHelperUpdater implements Runnable {

    private static final String JSON_URL = "https://raw.githubusercontent.com/"
                                           + "ThexXTURBOXx/UpdateJSONs/master/block-helper.csv";

    public boolean notify = false;
    private boolean error = false;
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
            List<String> latestVersions = getLatestModVersions(new URL(JSON_URL).openStream());
            if (latestVersions.isEmpty()) {
                throw new IllegalStateException("Version not found.");
            } else {
                latestVersion = latestVersions.get(0);
                isLatestVersion = latestVersions.contains(mod_BlockHelper.VERSION);
                if (isLatestVersion) {
                    mod_BlockHelper.LOG.info(I18n.translate("waila.newest_version_installed",
                            mod_BlockHelper.NAME));
                } else {
                    mod_BlockHelper.LOG.info(I18n.translate("waila.newer_version_available",
                            mod_BlockHelper.NAME, latestVersion));
                }
            }
        } catch (Throwable t) {
            error = true;
            mod_BlockHelper.LOG.log(Level.WARNING, I18n.translate("waila.update_check_failed",
                    mod_BlockHelper.NAME), t);
        }
    }

    /**
     * @return whether the updater has failed to check for updates.
     */
    public boolean isErrored() {
        return error;
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

    private List<String> getLatestModVersions(InputStream is) throws IOException {
        List<String> versions = new ArrayList<String>();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        try {
            String line;
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                if (mod_BlockHelper.MC_VERSION.equals(split[0]))
                    versions.add(split[1]);
            }
        } finally {
            is.close();
            isr.close();
            br.close();
        }
        return versions;
    }

    public void notifyUpdater(Minecraft mc) {
        if (!notify) return;
        if (isErrored())
            mc.thePlayer.addChatMessage(I18n.translate("waila.update_check_failed_chat",
                    mod_BlockHelper.NAME));
        else if (!isLatestVersion())
            mc.thePlayer.addChatMessage(I18n.translate("waila.newer_version_available_chat",
                    mod_BlockHelper.NAME, mod_BlockHelper.VERSION, getLatestVersion()));
    }

}
