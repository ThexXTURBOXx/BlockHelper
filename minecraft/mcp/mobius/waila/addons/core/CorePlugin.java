package mcp.mobius.waila.addons.core;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;

public final class CorePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new CorePlugin();

    static Field curBlockDamageMP;

    private CorePlugin() {
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public void register(IRegistrar registrar, Side side) {
        if (side.isClient()) {
            try {
                curBlockDamageMP = PlayerControllerMP.class.getDeclaredField("g");
                curBlockDamageMP.setAccessible(true);
            } catch (Throwable t) {
                try {
                    curBlockDamageMP = PlayerControllerMP.class.getDeclaredField("field_78770_f");
                    curBlockDamageMP.setAccessible(true);
                } catch (Throwable t1) {
                    try {
                        curBlockDamageMP = PlayerControllerMP.class.getDeclaredField("curBlockDamageMP");
                        curBlockDamageMP.setAccessible(true);
                    } catch (Throwable t2) {
                        throw new RuntimeException(t2);
                    }
                }
            }
        }

        registrar.registerNBTProvider(HUDHandlerBlocks.INSTANCE, Block.class);
        registrar.registerNBTProvider(HUDHandlerEntities.INSTANCE, Entity.class);

        registrar.addSyncedConfig("General", "general.invisibleplayers");

        if (side.isClient()) {
            registrar.addConfig("General", "general.showcrop");
            registrar.registerBodyProvider(HUDHandlerCrops.INSTANCE, Block.class);
        }

        if (side.isClient()) {
            registrar.addConfig("General", "general.harvest");
            registrar.addConfig("General", "general.lightlevel");
            registrar.addConfig("General", "general.break");

            registrar.registerHeadProvider(HUDHandlerBlocks.INSTANCE, Block.class);
            registrar.registerBodyProvider(HUDHandlerBlocks.INSTANCE, Block.class);
            registrar.registerTailProvider(HUDHandlerBlocks.INSTANCE, Block.class);

            registrar.addConfig("General", "general.showents");

            registrar.registerHeadProvider(HUDHandlerEntities.INSTANCE, Entity.class);
            registrar.registerTailProvider(HUDHandlerEntities.INSTANCE, Entity.class);
            registrar.registerStackProvider(HUDHandlerEntities.INSTANCE, Entity.class);

            if (mod_BlockHelper.DEV_MODE) {
                registrar.addConfig("General", "general.dev", false);

                registrar.registerBodyProvider(HUDHandlerDev.INSTANCE, Block.class);
                registrar.registerBodyProvider(HUDHandlerEntitiesDev.INSTANCE, Entity.class);
            }
        }
    }

}
