package mcp.mobius.waila.server;

import cpw.mods.fml.common.Loader;
import mcp.mobius.waila.addons.advmachines.AdvMachinesModule;
import mcp.mobius.waila.addons.advsolars.AdvSolarsModule;
import mcp.mobius.waila.addons.appeng.AppEngModule;
import mcp.mobius.waila.addons.buildcraft.BCModule;
import mcp.mobius.waila.addons.core.DecoratorFMP;
import mcp.mobius.waila.addons.core.HUDHandlerFMP;
import mcp.mobius.waila.addons.ee.EEModule;
import mcp.mobius.waila.addons.enderstorage.EnderStorageModule;
import mcp.mobius.waila.addons.forge.ForgeModule;
import mcp.mobius.waila.addons.harvestcraft.HarvestcraftModule;
import mcp.mobius.waila.addons.ic2.IC2Module;
import mcp.mobius.waila.addons.projectred.ProjectRedModule;
import mcp.mobius.waila.addons.railcraft.RailcraftModule;
import mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionModule;
import mcp.mobius.waila.addons.vanillamc.HUDHandlerCrops;
import mcp.mobius.waila.addons.vanillamc.HUDHandlerEntities;
import mcp.mobius.waila.addons.vanillamc.HUDHandlerFurnace;
import mcp.mobius.waila.addons.vanillamc.HUDHandlerVanilla;

public class ProxyServer {

    public ProxyServer() {
    }

    public void registerHandlers() {
    }

    public void registerMods() {

        HUDHandlerEntities.register();
        HUDHandlerVanilla.register();
        HUDHandlerCrops.register();
        HUDHandlerFurnace.register();

        /* ADVANCED MACHINES */
        AdvMachinesModule.register();

        /* ADVANCED SOLAR PANELS */
        AdvSolarsModule.register();

        /* APPLIED ENERGISTICS */
        AppEngModule.register();

        /* BUILDCRAFT */
        BCModule.register();

        /* EQUIVALENT EXCHANGE */
        EEModule.register();

        /* FORGE */
        ForgeModule.register();

        /* INDUSTRIALCRAFT2 */
        IC2Module.register();

        /*EnderStorage*/
        EnderStorageModule.register();

        /* Thermal Expansion */
        ThermalExpansionModule.register();

        /* ProjectRed API */
        ProjectRedModule.register();

        /* Railcraft */
        RailcraftModule.register();

        /* Pam's HarvestCraft */
        HarvestcraftModule.register();

        if (Loader.isModLoaded("ForgeMultipart")) {
            HUDHandlerFMP.register();
            DecoratorFMP.register();
        }
    }

}
