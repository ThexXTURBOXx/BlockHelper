package mcp.mobius.waila.proxy;

import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.List;
import mcp.mobius.waila.addons.advmachines.AdvMachinesPlugin;
import mcp.mobius.waila.addons.advsolars.AdvSolarsPlugin;
import mcp.mobius.waila.addons.appeng.AppEngPlugin;
import mcp.mobius.waila.addons.bc3.BC3Plugin;
import mcp.mobius.waila.addons.core.CorePlugin;
import mcp.mobius.waila.addons.ee.EEPlugin;
import mcp.mobius.waila.addons.enderstorage.EnderStoragePlugin;
import mcp.mobius.waila.addons.fmp.FMPPlugin;
import mcp.mobius.waila.addons.forge.ForgePlugin;
import mcp.mobius.waila.addons.harvestcraft.HarvestcraftPlugin;
import mcp.mobius.waila.addons.ic2.IC2Plugin;
import mcp.mobius.waila.addons.natura.NaturaPlugin;
import mcp.mobius.waila.addons.railcraft.RailcraftPlugin;
import mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionPlugin;
import mcp.mobius.waila.addons.vanilla.VanillaPlugin;
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
        registerPlugin(FMPPlugin.INSTANCE);
        registerPlugin(VanillaPlugin.INSTANCE);
        registerPlugin(AdvMachinesPlugin.INSTANCE);
        registerPlugin(AdvSolarsPlugin.INSTANCE);
        registerPlugin(AppEngPlugin.INSTANCE);
        registerPlugin(BC3Plugin.INSTANCE);
        registerPlugin(EEPlugin.INSTANCE);
        registerPlugin(EnderStoragePlugin.INSTANCE);
        registerPlugin(HarvestcraftPlugin.INSTANCE);
        registerPlugin(IC2Plugin.INSTANCE);
        registerPlugin(NaturaPlugin.INSTANCE);
        registerPlugin(RailcraftPlugin.INSTANCE);
        registerPlugin(ThermalExpansionPlugin.INSTANCE);
        registerPlugin(ForgePlugin.INSTANCE);
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
