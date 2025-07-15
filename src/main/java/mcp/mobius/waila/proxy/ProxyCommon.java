package mcp.mobius.waila.proxy;

import cpw.mods.fml.relauncher.Side;
import java.util.ArrayList;
import java.util.List;
import mcp.mobius.waila.addons.advmachines.AdvMachinesPlugin;
import mcp.mobius.waila.addons.advsolars.AdvSolarsPlugin;
import mcp.mobius.waila.addons.appeng.AppEngPlugin;
import mcp.mobius.waila.addons.barrels.BarrelsPlugin;
import mcp.mobius.waila.addons.bc3.BC3Plugin;
import mcp.mobius.waila.addons.cc.ChickenChunksPlugin;
import mcp.mobius.waila.addons.core.CorePlugin;
import mcp.mobius.waila.addons.ee2.EE2Plugin;
import mcp.mobius.waila.addons.ee3.EE3Plugin;
import mcp.mobius.waila.addons.enderstorage.EnderStoragePlugin;
import mcp.mobius.waila.addons.fmp.FMPPlugin;
import mcp.mobius.waila.addons.forge.ForgePlugin;
import mcp.mobius.waila.addons.harvestcraft.HarvestcraftPlugin;
import mcp.mobius.waila.addons.ic2.IC2Plugin;
import mcp.mobius.waila.addons.natura.NaturaPlugin;
import mcp.mobius.waila.addons.projectzulu.ProjectZuluPlugin;
import mcp.mobius.waila.addons.railcraft.RailcraftPlugin;
import mcp.mobius.waila.addons.thaumcraft.ThaumcraftPlugin;
import mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionPlugin;
import mcp.mobius.waila.addons.totalpanels.TotalPanelsPlugin;
import mcp.mobius.waila.addons.twilightforest.TwilightForestPlugin;
import mcp.mobius.waila.addons.vanilla.VanillaPlugin;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.event.WailaRegisterEvent;
import net.minecraftforge.common.MinecraftForge;

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
        registerCorePlugin(FMPPlugin.INSTANCE);
        registerCorePlugin(VanillaPlugin.INSTANCE);

        registerPlugin(AdvMachinesPlugin.INSTANCE);
        registerPlugin(AdvSolarsPlugin.INSTANCE);
        registerPlugin(AppEngPlugin.INSTANCE);
        registerPlugin(BarrelsPlugin.INSTANCE);
        registerPlugin(BC3Plugin.INSTANCE);
        registerPlugin(ChickenChunksPlugin.INSTANCE);
        registerPlugin(EE2Plugin.INSTANCE);
        registerPlugin(EE3Plugin.INSTANCE);
        registerPlugin(EnderStoragePlugin.INSTANCE);
        registerPlugin(HarvestcraftPlugin.INSTANCE);
        registerPlugin(IC2Plugin.INSTANCE);
        registerPlugin(NaturaPlugin.INSTANCE);
        registerPlugin(ProjectZuluPlugin.INSTANCE);
        registerPlugin(RailcraftPlugin.INSTANCE);
        registerPlugin(ThaumcraftPlugin.INSTANCE);
        registerPlugin(ThermalExpansionPlugin.INSTANCE);
        registerPlugin(TotalPanelsPlugin.INSTANCE);
        registerPlugin(TwilightForestPlugin.INSTANCE);
        registerPlugin(ForgePlugin.INSTANCE);
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
            !MinecraftForge.EVENT_BUS.post(new WailaRegisterEvent.Plugin(plugin)))
            plugin.register(registrar, side);
    }

    public void postLoad() {
    }

}
