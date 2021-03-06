package de.thexxturboxx.blockhelper;

import cpw.mods.fml.common.network.NetworkRegistry;
import de.thexxturboxx.blockhelper.integration.IntegrationRegistrar;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class BlockHelperCommonProxy {

    public EntityPlayer getPlayer() {
        return null;
    }

    public World getWorld() {
        return DimensionManager.getWorld(0);
    }

    public void load(mod_BlockHelper instance) {
        mod_BlockHelper.isClient = false;
        ModLoader.setInGameHook(instance, true, false);
        NetworkRegistry.instance().registerChannel(instance, mod_BlockHelper.CHANNEL);
        IntegrationRegistrar.init();
        Thread versionCheckThread = new Thread(new BlockHelperUpdater(), "Block Helper Version Check");
        versionCheckThread.start();
    }

}