package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.i18n.I18n;
import de.thexxturboxx.blockhelper.integration.IntegrationRegistrar;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.mod_BlockHelper;

public class BlockHelperCommonProxy {

    public void load(mod_BlockHelper instance) {
        I18n.init();
        MinecraftForge.registerConnectionHandler(instance);
        ModLoader.setInGameHook(instance, true, false);
        ModLoader.registerPacketChannel(instance, mod_BlockHelper.CHANNEL);
        IntegrationRegistrar.init();
        Thread versionCheckThread = new Thread(new BlockHelperUpdater(), "Block Helper Version Check");
        versionCheckThread.start();
    }

}
