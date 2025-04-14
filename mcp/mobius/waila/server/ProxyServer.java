package mcp.mobius.waila.server;

import java.util.ArrayList;
import java.util.List;
import mcp.mobius.waila.addons.advmachines.AdvMachinesPlugin;
import mcp.mobius.waila.addons.advsolars.AdvSolarsPlugin;
import mcp.mobius.waila.addons.appeng.AppEngPlugin;
import mcp.mobius.waila.addons.buildcraft.BCPlugin;
import mcp.mobius.waila.addons.core.CorePlugin;
import mcp.mobius.waila.addons.ee.EEPlugin;
import mcp.mobius.waila.addons.enderstorage.EnderStoragePlugin;
import mcp.mobius.waila.addons.forge.ForgePlugin;
import mcp.mobius.waila.addons.harvestcraft.HarvestcraftPlugin;
import mcp.mobius.waila.addons.ic2.IC2Plugin;
import mcp.mobius.waila.addons.projectred.ProjectRedPlugin;
import mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionPlugin;
import mcp.mobius.waila.addons.vanilla.VanillaPlugin;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;

public class ProxyServer {

    protected final List<IWailaPlugin> plugins = new ArrayList<IWailaPlugin>();

    public ProxyServer() {
    }

    public void prepare() {
        plugins.add(VanillaPlugin.INSTANCE);
        plugins.add(AdvMachinesPlugin.INSTANCE);
        plugins.add(AdvSolarsPlugin.INSTANCE);
        plugins.add(AppEngPlugin.INSTANCE);
        plugins.add(BCPlugin.INSTANCE);
        plugins.add(EEPlugin.INSTANCE);
        plugins.add(EnderStoragePlugin.INSTANCE);
        plugins.add(ForgePlugin.INSTANCE);
        plugins.add(HarvestcraftPlugin.INSTANCE);
        plugins.add(IC2Plugin.INSTANCE);
        plugins.add(ProjectRedPlugin.INSTANCE);
        plugins.add(ThermalExpansionPlugin.INSTANCE);
    }

    public void registerCorePlugins(IRegistrar registrar) {
        CorePlugin.INSTANCE.registerCommon(registrar);
    }

    public void registerModPlugins(IRegistrar registrar) {
        for (IWailaPlugin plugin : plugins)
            plugin.registerCommon(registrar);
    }

}
