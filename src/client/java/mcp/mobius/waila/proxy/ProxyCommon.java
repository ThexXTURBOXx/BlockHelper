package mcp.mobius.waila.proxy;

import java.util.ArrayList;
import java.util.List;
import mcp.mobius.waila.addons.advsolars.AdvSolarsPlugin;
import mcp.mobius.waila.addons.bc2.BC2Plugin;
import mcp.mobius.waila.addons.core.CorePlugin;
import mcp.mobius.waila.addons.ee.EEPlugin;
import mcp.mobius.waila.addons.ee2.EE2Plugin;
import mcp.mobius.waila.addons.ee3.EE3Plugin;
import mcp.mobius.waila.addons.florasoma.FloraSomaPlugin;
import mcp.mobius.waila.addons.ic.ICPlugin;
import mcp.mobius.waila.addons.ic2.IC2Plugin;
import mcp.mobius.waila.addons.redpower.RedPowerPlugin;
import mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionPlugin;
import mcp.mobius.waila.addons.vanilla.VanillaPlugin;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.event.WailaEventRegistrar;
import mcp.mobius.waila.api.event.WailaRegisterEvent;

public class ProxyCommon {

    private final List<IWailaPlugin> corePlugins = new ArrayList<IWailaPlugin>();
    private final List<IWailaPlugin> plugins = new ArrayList<IWailaPlugin>();

    public ProxyCommon() {
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

        registerPlugin(AdvSolarsPlugin.INSTANCE);
        registerPlugin(EEPlugin.INSTANCE);
        registerPlugin(EE2Plugin.INSTANCE);
        registerPlugin(EE3Plugin.INSTANCE);
        registerPlugin(FloraSomaPlugin.INSTANCE);
        registerPlugin(ICPlugin.INSTANCE);
        registerPlugin(IC2Plugin.INSTANCE);
        registerPlugin(RedPowerPlugin.INSTANCE);
        registerPlugin(ThermalExpansionPlugin.INSTANCE);
        registerPlugin(BC2Plugin.INSTANCE);
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
            plugin.register(registrar);
    }

    public void postLoad() {
    }

}
