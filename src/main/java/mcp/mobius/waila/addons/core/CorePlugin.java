package mcp.mobius.waila.addons.core;

import cpw.mods.fml.relauncher.Side;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.utils.AccessHelper;
import net.minecraft.block.Block;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.src.mod_BlockHelper;

public final class CorePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new CorePlugin();

    static Field curBlockDamageMP;

    static Method getDropItemId;

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
                curBlockDamageMP = AccessHelper.getDeclaredField(PlayerControllerMP.class,
                        "g", "field_78770_f", "curBlockDamageMP");
                getDropItemId = AccessHelper.getDeclaredMethod(EntityLiving.class, new Class[0],
                        "bb", "func_70633_aT", "getDropItemId");
            } catch (Throwable t) {
                throw new RuntimeException(t);
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
