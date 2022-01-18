package de.thexxturboxx.blockhelper.fix;

import cpw.mods.fml.relauncher.RelaunchClassLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.FontRenderer;
import net.minecraft.src.mod_BlockHelper;

public final class FixDetector {

    private FixDetector() {
        throw new UnsupportedOperationException();
    }

    public static void detectFixes(Minecraft mc) {
        try {
            if (!RelaunchClassLoader.FIXER_VERSION.equals("1")) {
                String name = mod_BlockHelper.NAME;
                mc.thePlayer.addChatMessage("§7[§6" + name + "§7] §cPlease update ClassLoaderFixer.");
                mc.thePlayer.addChatMessage("§cYou can find it on Modrinth.");
            }
        } catch (Throwable t) {
            String name = mod_BlockHelper.NAME;
            mc.thePlayer.addChatMessage("§7[§6" + name + "§7] §cIt is very recommended to");
            mc.thePlayer.addChatMessage("§cinstall the ClassLoaderFixer jar-mod. You can find it on Modrinth.");
            mc.thePlayer.addChatMessage("§cOtherwise, mod identification might not work correctly.");
        }

        try {
            if (!FontRenderer.FIXER_VERSION.equals("1")) {
                String name = mod_BlockHelper.NAME;
                mc.thePlayer.addChatMessage("§7[§6" + name + "§7] §cPlease update FontFixer.");
                mc.thePlayer.addChatMessage("§cYou can find it on Modrinth.");
            }
        } catch (Throwable t) {
            String name = mod_BlockHelper.NAME;
            mc.thePlayer.addChatMessage("§7[§6" + name + "§7] §cIt is very recommended to");
            mc.thePlayer.addChatMessage("§cinstall the FontFixer jar-mod. You can find it on Modrinth.");
            mc.thePlayer.addChatMessage("§cOtherwise, some texts may not be rendered correctly.");
        }
    }

}
