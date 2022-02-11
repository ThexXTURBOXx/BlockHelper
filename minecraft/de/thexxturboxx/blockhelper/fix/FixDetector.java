package de.thexxturboxx.blockhelper.fix;

import de.thexxturboxx.blockhelper.BlockHelperClientProxy;
import de.thexxturboxx.blockhelper.FontFixer;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.mod_BlockHelper;

public final class FixDetector {

    private FixDetector() {
        throw new UnsupportedOperationException();
    }

    public static void detectFixes(Minecraft mc) {
        if (!BlockHelperClientProxy.fixerNotify) {
            return;
        }

        try {
            if (!FontRenderer.FIXER_VERSION.equals("1")) {
                String name = mod_BlockHelper.NAME;
                mc.thePlayer.addChatMessage("\u00a77[\u00a76" + name + "\u00a77] \u00a7cPlease update FontFixer.");
                mc.thePlayer.addChatMessage("\u00a7cYou can find it on Modrinth.");
            }
        } catch (Throwable t) {
            try {
                if (!FontFixer.FIXER_VERSION.equals("1")) {
                    String name = mod_BlockHelper.NAME;
                    mc.thePlayer.addChatMessage("\u00a77[\u00a76" + name + "\u00a77] \u00a7cPlease update FontFixer.");
                    mc.thePlayer.addChatMessage("\u00a7cYou can find it on Modrinth.");
                }
            } catch (Throwable t1) {
                String name = mod_BlockHelper.NAME;
                mc.thePlayer.addChatMessage("\u00a77[\u00a76" + name + "\u00a77] \u00a7cIt is very recommended to");
                mc.thePlayer.addChatMessage("\u00a7cinstall the FontFixer jar-mod. You can find it on Modrinth.");
                mc.thePlayer.addChatMessage("\u00a7cOtherwise, some texts may not be rendered correctly.");
            }
        }
    }

}