package mcp.mobius.waila.addons.core;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import net.minecraft.src.Block;
import net.minecraft.src.Entity;

public final class CorePlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new CorePlugin();

    private CorePlugin() {
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerNBTProvider(HUDHandlerBlocks.INSTANCE, Block.class);
        registrar.registerNBTProvider(HUDHandlerEntities.INSTANCE, Entity.class);
    }

}
