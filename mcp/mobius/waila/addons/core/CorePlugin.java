package mcp.mobius.waila.addons.core;

import java.lang.reflect.Field;
import java.util.logging.Level;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IEntityProvider;
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
    public void registerCommon(IRegistrar registrar) {
        try {
            Class<?> BlockMultipart = Class.forName("codechicken.multipart.BlockMultipart");
            registrar.registerDecorator(new DecoratorFMP(), BlockMultipart);
        } catch (ClassNotFoundException e) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[FMP] Class not found. ", e);
        } catch (Throwable t) {
            mod_BlockHelper.LOG.log(Level.WARNING, "[FMP] Unhandled exception.", t);
        }

        {
            Class<?> BlockMultipart;
            try {
                BlockMultipart = Class.forName("codechicken.multipart.BlockMultipart");
            } catch (ClassNotFoundException e) {
                mod_BlockHelper.LOG.log(Level.WARNING, "[FMP] Class not found. ", e);
                return;
            } catch (Throwable t) {
                mod_BlockHelper.LOG.log(Level.WARNING, "[FMP] Unhandled exception.", t);
                return;
            }

            IDataProvider provider = new HUDHandlerFMP();
            registrar.registerHeadProvider(provider, BlockMultipart);
            registrar.registerBodyProvider(provider, BlockMultipart);
            registrar.registerTailProvider(provider, BlockMultipart);
            registrar.registerNBTProvider(provider, BlockMultipart);

            mod_BlockHelper.LOG.log(Level.INFO, "Forge Multipart found and dedicated handler registered");
        }
    }

    @Override
    public void registerClient(IRegistrar registrar) {
        {
            registrar.addConfig("General", "general.harvest");
            registrar.addConfig("General", "general.lightlevel");
            registrar.addConfig("General", "general.break");

            IDataProvider provider = new HUDHandlerBlocks();
            registrar.registerHeadProvider(provider, Block.class);
            registrar.registerBodyProvider(provider, Block.class);
            registrar.registerTailProvider(provider, Block.class);

            try {
                curBlockDamageMP = PlayerControllerMP.class.getDeclaredField("curBlockDamageMP");
                curBlockDamageMP.setAccessible(true);
            } catch (Throwable t) {
                try {
                    curBlockDamageMP = PlayerControllerMP.class.getDeclaredField("field_78770_f");
                    curBlockDamageMP.setAccessible(true);
                } catch (Throwable t1) {
                    throw new RuntimeException(t1);
                }
            }
        }

        {
            registrar.addConfig("General", "general.showents");

            IEntityProvider provider = new HUDHandlerEntities();
            registrar.registerHeadProvider(provider, Entity.class);
            registrar.registerTailProvider(provider, Entity.class);
            registrar.registerStackProvider(provider, Entity.class);
        }

        if (mod_BlockHelper.DEV_MODE) {
            registrar.addConfig("General", "general.dev", false);
            registrar.registerBodyProvider(new HUDHandlerDev(), Block.class);
            registrar.registerBodyProvider(new HUDHandlerEntitiesDev(), Entity.class);
        }
    }

}
