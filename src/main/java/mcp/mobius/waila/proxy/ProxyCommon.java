package mcp.mobius.waila.proxy;

import cpw.mods.fml.common.Side;
import java.util.ArrayList;
import java.util.List;
import mcp.mobius.waila.addons.advmachines.as.AdvMachinesASPlugin;
import mcp.mobius.waila.addons.advmachines.synke.AdvMachinesSnykePlugin;
import mcp.mobius.waila.addons.advsolars.AdvSolarsPlugin;
import mcp.mobius.waila.addons.bc3.BC3Plugin;
import mcp.mobius.waila.addons.core.CorePlugin;
import mcp.mobius.waila.addons.ee2.EE2Plugin;
import mcp.mobius.waila.addons.ee3.EE3Plugin;
import mcp.mobius.waila.addons.enderstorage.EnderStoragePlugin;
import mcp.mobius.waila.addons.florasoma.FloraSomaPlugin;
import mcp.mobius.waila.addons.harvestcraft.HarvestcraftPlugin;
import mcp.mobius.waila.addons.ic2.IC2Plugin;
import mcp.mobius.waila.addons.railcraft.RailcraftPlugin;
import mcp.mobius.waila.addons.redpower2.RedPower2Plugin;
import mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionPlugin;
import mcp.mobius.waila.addons.vanilla.VanillaPlugin;
import mcp.mobius.waila.addons.weeeflowers.WeeeFlowersPlugin;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;

public class ProxyCommon {

    private final Side side;
    private final List<IWailaPlugin> plugins = new ArrayList<IWailaPlugin>();

    public ProxyCommon(Side side) {
        this.side = side;
    }

    public void registerPlugin(IWailaPlugin plugin) {
        plugins.add(plugin);
    }

    public void prepare() {
        registerPlugin(CorePlugin.INSTANCE);
        registerPlugin(VanillaPlugin.INSTANCE);
        registerPlugin(AdvMachinesASPlugin.INSTANCE);
        registerPlugin(AdvMachinesSnykePlugin.INSTANCE);
        registerPlugin(AdvSolarsPlugin.INSTANCE);
        registerPlugin(BC3Plugin.INSTANCE);
        registerPlugin(EE2Plugin.INSTANCE);
        registerPlugin(EE3Plugin.INSTANCE);
        registerPlugin(EnderStoragePlugin.INSTANCE);
        registerPlugin(FloraSomaPlugin.INSTANCE);
        registerPlugin(HarvestcraftPlugin.INSTANCE);
        registerPlugin(IC2Plugin.INSTANCE);
        registerPlugin(RailcraftPlugin.INSTANCE);
        registerPlugin(RedPower2Plugin.INSTANCE);
        registerPlugin(ThermalExpansionPlugin.INSTANCE);
        registerPlugin(WeeeFlowersPlugin.INSTANCE);
    }

    public void registerCorePlugins(IRegistrar registrar) {
    }

    public void registerModPlugins(IRegistrar registrar) {
        for (IWailaPlugin plugin : plugins)
            if (plugin.shouldRegister())
                plugin.register(registrar, side);
    }

    public void postLoad() {
    }

}
