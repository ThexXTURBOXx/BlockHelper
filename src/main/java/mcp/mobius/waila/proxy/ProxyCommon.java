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
import mcp.mobius.waila.addons.forge.ForgePlugin;
import mcp.mobius.waila.addons.harvestcraft.HarvestcraftPlugin;
import mcp.mobius.waila.addons.ic2.IC2Plugin;
import mcp.mobius.waila.addons.railcraft.RailcraftPlugin;
import mcp.mobius.waila.addons.redpower2.RedPower2Plugin;
import mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionPlugin;
import mcp.mobius.waila.addons.vanilla.VanillaPlugin;
import mcp.mobius.waila.addons.weeeflowers.WeeeFlowersPlugin;
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
        registerPlugin(ForgePlugin.INSTANCE);
    }

    public void registerCorePlugins(IRegistrar registrar) {
        registerCorePlugin(CorePlugin.INSTANCE);
        registerCorePlugin(VanillaPlugin.INSTANCE);
    }

    public void registerModPlugins(IRegistrar registrar) {
        for (IWailaPlugin plugin : corePlugins)
            registerPluginInRegistrar(registrar, plugin);
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
