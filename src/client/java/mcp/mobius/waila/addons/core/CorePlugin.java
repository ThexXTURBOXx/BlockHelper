package mcp.mobius.waila.addons.core;

import java.lang.reflect.Method;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.mod_BlockHelper;

public final class CorePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new CorePlugin();

    static Method getDropItemId;

    private CorePlugin() {
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public void register(IRegistrar registrar) {
        try {
            getDropItemId = AccessHelper.getDeclaredMethod(EntityLiving.class, new Class[0],
                    "g_", "getDropItemId");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }

        registrar.registerNBTProvider(HUDHandlerBlocks.INSTANCE, Block.class);
        registrar.registerNBTProvider(HUDHandlerEntities.INSTANCE, Entity.class);

        registrar.addConfig("General", "general.showcrop");
        registrar.registerBodyProvider(HUDHandlerCrops.INSTANCE, Block.class);

        registrar.addConfig("General", "general.harvest");
        registrar.addConfig("General", "general.lightlevel");
        registrar.addConfig("General", "general.break");
        registrar.addConfig("General", "general.oldlightlevelol", false);

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
