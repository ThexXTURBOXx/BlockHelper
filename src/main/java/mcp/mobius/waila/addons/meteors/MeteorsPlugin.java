package mcp.mobius.waila.addons.meteors;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.addons.vanilla.StackDropFixer;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.AccessHelper;

public final class MeteorsPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new MeteorsPlugin();

    static Field MeteorsMod_instance = null;
    static Field MeteorsMod_ShieldRadiusMultiplier = null;

    static Field BlockMeteorShieldTorch_torchActive = null;

    private MeteorsPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        try {
            Class<?> MeteorsMod = AccessHelper.getClass("net.meteor.common.MeteorsMod");
            MeteorsMod_instance = AccessHelper.getField(MeteorsMod, "instance");
            MeteorsMod_ShieldRadiusMultiplier = AccessHelper.getField(MeteorsMod, "ShieldRadiusMultiplier");

            mod_BlockHelper.LOG.log(Level.INFO, "[Falling Meteors] Mod found.");
            return true;
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.INFO, "[Falling Meteors] Mod not found.");
        }
        return false;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        try {
            Class<?> BlockMeteorShieldTorch = AccessHelper.getClass("net.meteor.common.block.BlockMeteorShieldTorch");
            BlockMeteorShieldTorch_torchActive = AccessHelper.getDeclaredField(BlockMeteorShieldTorch, "torchActive");

            registrar.addConfig("Falling Meteors", "fm.torch");

            if (side.isClient()) {
                registrar.registerStackProvider(StackDropFixer.DEFAULT, BlockMeteorShieldTorch);

                registrar.registerBodyProvider(HUDHandlerTorch.INSTANCE, BlockMeteorShieldTorch);
            }
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Falling Meteors] Error while loading torch hooks.", t);
        }

        try {
            Class<?> TileEntityMeteorShield =
                    AccessHelper.getClass("net.meteor.common.tileentity.TileEntityMeteorShield");

            registrar.addSyncedConfig("Falling Meteors", "fm.radius");

            if (side.isClient())
                registrar.registerBodyProvider(HUDHandlerShield.INSTANCE, TileEntityMeteorShield);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[Falling Meteors] Error while loading shield hooks.", t);
        }
    }

}
