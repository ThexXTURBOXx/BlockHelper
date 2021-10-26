package de.thexxturboxx.blockhelper.fix;

import cpw.mods.fml.relauncher.RelaunchClassLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.src.mod_BlockHelper;

public final class FixDetector {

    private FixDetector() {
        throw new UnsupportedOperationException();
    }

    public static void detectFixes(Minecraft mc) {
        try {
            if (!RelaunchClassLoader.FIXER_VERSION.equals("1")) {
                String name = new mod_BlockHelper().getName();
                mc.thePlayer.addChatMessage("§7[§6" + name + "§7] §cPlease update ClassLoaderFixer.");
                mc.thePlayer.addChatMessage("§cYou can find it here: §chttps://git.io/Jiljq");
            }
        } catch (Throwable t) {
            String name = new mod_BlockHelper().getName();
            mc.thePlayer.addChatMessage("§7[§6" + name + "§7] §cIt is very recommended to");
            mc.thePlayer.addChatMessage("§cinstall the ClassLoaderFixer jar-mod. You can find it here:");
            mc.thePlayer.addChatMessage("§chttps://git.io/Jiljq");
            mc.thePlayer.addChatMessage("§cOtherwise, mod identification might not work correctly.");
        }
    }

}
