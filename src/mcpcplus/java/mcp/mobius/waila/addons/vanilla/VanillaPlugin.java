package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import net.minecraft.server.EntityLiving;

public final class VanillaPlugin implements IWailaPlugin {

    public static final IWailaPlugin INSTANCE = new VanillaPlugin();

    private VanillaPlugin() {
    }

    @Override
    public boolean shouldRegister() {
        return true;
    }

    @Override
    public void register(IRegistrar registrar) {
        registrar.addSyncedConfig("VanillaMC", "vanilla.showhp");
        registrar.addSyncedConfig("VanillaMC", "vanilla.breed");
        registrar.addSyncedConfig("VanillaMC", "vanilla.tame");
        registrar.addSyncedConfig("VanillaMC", "vanilla.sheep");
        registrar.addSyncedConfig("VanillaMC", "vanilla.villager");
        registrar.addSyncedConfig("VanillaMC", "vanilla.tnt");

        registrar.registerNBTProvider(HUDHandlerEntities.INSTANCE, EntityLiving.class);

        registrar.addSyncedConfig("VanillaMC", "vanilla.furnace");
        registrar.addSyncedConfig("VanillaMC", "vanilla.jukebox");
        registrar.addSyncedConfig("VanillaMC", "vanilla.noteblock");
    }

}
