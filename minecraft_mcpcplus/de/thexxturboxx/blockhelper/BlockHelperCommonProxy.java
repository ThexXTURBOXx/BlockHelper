package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.i18n.I18n;
import de.thexxturboxx.blockhelper.integration.IntegrationRegistrar;
import forge.MinecraftForge;
import net.minecraft.server.ModLoader;
import net.minecraft.server.mod_BlockHelper;

public class BlockHelperCommonProxy {

    public void load(mod_BlockHelper instance) {
        I18n.init();
        MinecraftForge.registerConnectionHandler(instance);
        try {
            ModLoader.registerPacketChannel(instance, mod_BlockHelper.CHANNEL);
        } catch (Throwable ignored) {
        }
        IntegrationRegistrar.init();
        Thread versionCheckThread = new Thread(new BlockHelperUpdater(), "Block Helper Version Check");
        versionCheckThread.start();
    }

}
