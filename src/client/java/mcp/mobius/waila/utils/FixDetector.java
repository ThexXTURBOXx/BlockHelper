package mcp.mobius.waila.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;

import static mcp.mobius.waila.api.SpecialChars.GOLD;
import static mcp.mobius.waila.api.SpecialChars.GRAY;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static net.minecraft.src.mod_BlockHelper.NAME;

public final class FixDetector {

    public static boolean notify = false;

    private FixDetector() {
        throw new UnsupportedOperationException();
    }

    public static void detectFixes(Minecraft mc) {
        try {
            Object fixerVersion = AccessHelper.getField(FontRenderer.class, "FIXER_VERSION").get(null);
            if (!"2".equals(fixerVersion)) {
                mc.ingameGUI.addChatMessage(GRAY + "[" + GOLD + NAME + GRAY + "] " +
                                            RED + "Please update FontFixer.");
                mc.ingameGUI.addChatMessage(RED + "You can find it on Modrinth.");
            }
        } catch (Throwable t) {
            try {
                Class<?> FontFixer = AccessHelper.getClass("de.thexxturboxx.blockhelper.FontFixer");
                Object fixerVersion = AccessHelper.getField(FontFixer, "FIXER_VERSION").get(null);
                if (!"2".equals(fixerVersion)) {
                    mc.ingameGUI.addChatMessage(GRAY + "[" + GOLD + NAME + GRAY + "] " +
                                                RED + "Please update FontFixer.");
                    mc.ingameGUI.addChatMessage(RED + "You can find it on Modrinth.");
                }
            } catch (Throwable t1) {
                mc.ingameGUI.addChatMessage(GRAY + "[" + GOLD + NAME + GRAY + "] " +
                                            RED + "It is very recommended to install the");
                mc.ingameGUI.addChatMessage(RED + "FontFixer jar-mod. " +
                                            "You can find it on Modrinth.");
                mc.ingameGUI.addChatMessage(RED + "Otherwise, some texts " +
                                            "will not be rendered correctly!");
            }
        }
    }

}
