package mcp.mobius.waila.addons.core;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.mod_BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;

public final class CorePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new CorePlugin();

    static Field curBlockDamageMP;

    Class<?> BlockMultipart;

    private CorePlugin() {
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public void registerCommon(IRegistrar registrar) {
        registrar.addSyncedConfig("General", "general.insivisbleplayers");

        try {
            BlockMultipart = Class.forName("codechicken.multipart.BlockMultipart");
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.FINEST, "[FMP] Class not found.", e);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[FMP] Unhandled exception.", t);
        }

        if (BlockMultipart != null) {
            registrar.registerNBTProvider(HUDHandlerFMP.INSTANCE, BlockMultipart);

            mod_BlockHelper.LOG.log(Level.INFO, "Forge Multipart found and dedicated handler registered");
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
        registrar.addConfig("General", "general.harvest");
        registrar.addConfig("General", "general.lightlevel");
        registrar.addConfig("General", "general.break");

        registrar.registerHeadProvider(HUDHandlerBlocks.INSTANCE, Block.class);
        registrar.registerBodyProvider(HUDHandlerBlocks.INSTANCE, Block.class);
        registrar.registerTailProvider(HUDHandlerBlocks.INSTANCE, Block.class);

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

        registrar.addConfig("General", "general.showents");

        registrar.registerHeadProvider(HUDHandlerEntities.INSTANCE, Entity.class);
        registrar.registerTailProvider(HUDHandlerEntities.INSTANCE, Entity.class);
        registrar.registerStackProvider(HUDHandlerEntities.INSTANCE, Entity.class);

        if (BlockMultipart != null) {
            registrar.registerDecorator(DecoratorFMP.INSTANCE, BlockMultipart);

            registrar.registerHeadProvider(HUDHandlerFMP.INSTANCE, BlockMultipart);
            registrar.registerBodyProvider(HUDHandlerFMP.INSTANCE, BlockMultipart);
            registrar.registerTailProvider(HUDHandlerFMP.INSTANCE, BlockMultipart);
        }

        if (mod_BlockHelper.DEV_MODE) {
            registrar.addConfig("General", "general.dev", false);

            registrar.registerBodyProvider(HUDHandlerDev.INSTANCE, Block.class);
            registrar.registerBodyProvider(HUDHandlerEntitiesDev.INSTANCE, Entity.class);
        }
    }

}
