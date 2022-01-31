package de.thexxturboxx.blockhelper.fix;

import de.thexxturboxx.blockhelper.BlockHelperClientProxy;
import net.minecraft.client.Minecraft;

public final class FixDetector {

    private FixDetector() {
        throw new UnsupportedOperationException();
    }

    public static void detectFixes(Minecraft mc) {
        if (!BlockHelperClientProxy.fixerNotify) {
            return;
        }
    }

}
