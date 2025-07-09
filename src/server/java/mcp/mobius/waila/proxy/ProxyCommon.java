package mcp.mobius.waila.proxy;

import cpw.mods.fml.common.Side;
import java.util.ArrayList;
import java.util.List;
import mcp.mobius.waila.addons.advmachines.as.AdvMachinesASPlugin;
import mcp.mobius.waila.addons.advsolars.AdvSolarsPlugin;
import mcp.mobius.waila.addons.bc2.BC2Plugin;
import mcp.mobius.waila.addons.bc3.BC3Plugin;
import mcp.mobius.waila.addons.core.CorePlugin;
import mcp.mobius.waila.addons.ic2.IC2Plugin;
import mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionPlugin;
import mcp.mobius.waila.addons.vanilla.VanillaPlugin;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.event.WailaEventRegistrar;
import mcp.mobius.waila.api.event.WailaRegisterEvent;

public class ProxyCommon {

    private final Side side;
    private final List<IWailaPlugin> corePlugins = new ArrayList<IWailaPlugin>();
    private final List<IWailaPlugin> plugins = new ArrayList<IWailaPlugin>();

    public ProxyCommon(Side side) {
        this.side = side;
    }

    // Do NOT use this outside of BlockHelper!
    private void registerCorePlugin(IWailaPlugin plugin) {
        corePlugins.add(plugin);
    }

    public void registerPlugin(IWailaPlugin plugin) {
        plugins.add(plugin);
    }

    public void prepare() {
        registerCorePlugin(CorePlugin.INSTANCE);
        registerCorePlugin(VanillaPlugin.INSTANCE);

        registerPlugin(AdvMachinesASPlugin.INSTANCE);
        registerPlugin(AdvSolarsPlugin.INSTANCE);
        registerPlugin(IC2Plugin.INSTANCE);
        registerPlugin(ThermalExpansionPlugin.INSTANCE);
        registerPlugin(BC2Plugin.INSTANCE);
        registerPlugin(BC3Plugin.INSTANCE);
    }

    public void registerCorePlugins(IRegistrar registrar) {
        for (IWailaPlugin plugin : corePlugins)
            registerPluginInRegistrar(registrar, plugin);
    }

    public void registerModPlugins(IRegistrar registrar) {
        for (IWailaPlugin plugin : plugins)
            registerPluginInRegistrar(registrar, plugin);
    }

    private void registerPluginInRegistrar(IRegistrar registrar, IWailaPlugin plugin) {
        if (plugin.shouldRegister() &&
            !WailaEventRegistrar.postPluginRegister(new WailaRegisterEvent.Plugin(plugin)))
            plugin.register(registrar, side);
    }

    public void postLoad() {
    }

}
