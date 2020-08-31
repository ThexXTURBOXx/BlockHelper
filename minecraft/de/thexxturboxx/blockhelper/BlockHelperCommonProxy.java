package de.thexxturboxx.blockhelper;

import cpw.mods.fml.common.FMLCommonHandler;
import de.thexxturboxx.blockhelper.integration.IntegrationRegistrar;
import net.minecraft.src.forge.DimensionManager;
import net.minecraft.src.forge.MinecraftForge;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.World;

public class BlockHelperCommonProxy {

    public EntityPlayer getPlayer() {
        return null;
    }

    public World getWorld() {
        return DimensionManager.getWorld(0);
    }

    public void load(mod_BlockHelper instance) {
        mod_BlockHelper.isClient = false;
        MinecraftForge.registerConnectionHandler(instance);
        ModLoader.setInGameHook(instance, true, false);
        FMLCommonHandler.instance().registerChannel(FMLCommonHandler.instance().findContainerFor(instance), mod_BlockHelper.CHANNEL);
        IntegrationRegistrar.init();
        Thread versionCheckThread = new Thread(new BlockHelperUpdater(), "Block Helper Version Check");
        versionCheckThread.start();
    }

}
