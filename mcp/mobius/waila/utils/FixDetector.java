package mcp.mobius.waila.utils;

import net.minecraft.client.Minecraft;

public final class FixDetector {

    public static boolean notify = false;

    private FixDetector() {
        throw new UnsupportedOperationException();
    }

    public static void detectFixes(Minecraft mc) {
        if (!notify) return;
    }

}
