package de.thexxturboxx.blockhelper;

import de.thexxturboxx.blockhelper.integration.IntegrationRegistrar;
import forge.DimensionManager;
import forge.MinecraftForge;
import net.minecraft.server.EntityPlayer;
import net.minecraft.server.ModLoader;
import net.minecraft.server.World;
import net.minecraft.server.mod_BlockHelper;

public class BlockHelperCommonProxy {

    public EntityPlayer getPlayer() {
        return null;
    }

    public World getWorld() {
        return DimensionManager.getWorld(0);
    }

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
