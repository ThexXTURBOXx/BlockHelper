package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.integration.IntegrationRegistrar;
import net.minecraft.src.ModLoader;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.mod_BlockHelper;

public class BlockHelperCommonProxy {

    public void load(mod_BlockHelper instance) {
        MinecraftForge.registerConnectionHandler(instance);
        ModLoader.setInGameHook(instance, true, false);
        try {
            ModLoader.registerPacketChannel(instance, mod_BlockHelper.CHANNEL);
        } catch (Throwable ignored) {
        }
        IntegrationRegistrar.init();
        Thread versionCheckThread = new Thread(new BlockHelperUpdater(), "Block Helper Version Check");
        versionCheckThread.start();
    }

}
