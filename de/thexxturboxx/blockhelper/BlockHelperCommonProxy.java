package de.thexxturboxx.blockhelper;

import cpw.mods.fml.common.network.NetworkRegistry;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.src.ModLoader;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class BlockHelperCommonProxy {

    public void registerRenderers() {
    }

    public EntityPlayer getPlayer() {
        return null;
    }

    public World getWorld() {
        return DimensionManager.getWorlds()[0];
    }

    public void load(mod_BlockHelper instance) {
        mod_BlockHelper.isClient = false;
        ModLoader.setInGameHook(instance, true, false);
        NetworkRegistry.instance().registerChannel(instance, mod_BlockHelper.CHANNEL);
        Thread versionCheckThread = new Thread(new BlockHelperUpdater(), "Block Helper Version Check");
        versionCheckThread.start();
    }

}