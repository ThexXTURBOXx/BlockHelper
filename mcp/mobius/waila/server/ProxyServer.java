package mcp.mobius.waila.server;

import cpw.mods.fml.common.Loader;
import java.lang.reflect.Method;
import mcp.mobius.waila.addons.advmachines.AdvMachinesModule;
import mcp.mobius.waila.addons.advsolars.AdvSolarsModule;
import mcp.mobius.waila.addons.appeng.AppEngModule;
import mcp.mobius.waila.addons.buildcraft.BCModule;
import mcp.mobius.waila.addons.enderstorage.EnderStorageModule;
import mcp.mobius.waila.addons.harvestcraft.HarvestcraftModule;
import mcp.mobius.waila.addons.ic2.IC2Module;
import mcp.mobius.waila.addons.projectred.ProjectRedModule;
import mcp.mobius.waila.addons.railcraft.RailcraftModule;
import mcp.mobius.waila.addons.thermalexpansion.ThermalExpansionModule;
import mcp.mobius.waila.addons.vanillamc.HUDHandlerEntities;
import mcp.mobius.waila.addons.vanillamc.HUDHandlerFurnace;
import mcp.mobius.waila.addons.vanillamc.HUDHandlerVanilla;
import mcp.mobius.waila.api.IWailaRegistrar;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.handlers.DecoratorFMP;
import mcp.mobius.waila.handlers.HUDHandlerFMP;
import mcp.mobius.waila.mod_BlockHelper;

public class ProxyServer {

    public ProxyServer() {
    }

    public void registerHandlers() {
    }

    public void registerMods() {

        HUDHandlerEntities.register();
        HUDHandlerVanilla.register();
        HUDHandlerFurnace.register();

        /* ADVANCED MACHINES */
        AdvMachinesModule.register();

        /* ADVANCED SOLAR PANELS */
        AdvSolarsModule.register();

        /* APPLIED ENERGISTICS */
        AppEngModule.register();

        /* BUILDCRAFT */
        BCModule.register();

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

        //ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerBlocks(),   Block.class);
        //ModuleRegistrar.instance().registerBodyProvider(new HUDHandlerBlocks(),   TileEntity.class);
    }

    public void callbackRegistration(String method, String modname) {
        String[] splitName = method.split("\\.");
        String methodName = splitName[splitName.length - 1];
        String className = method.substring(0, method.length() - methodName.length() - 1);

        mod_BlockHelper.log.info(String.format("Trying to reflect %s %s", className, methodName));

        try {
            Class<?> reflectClass = Class.forName(className);
            Method reflectMethod = reflectClass.getDeclaredMethod(methodName, IWailaRegistrar.class);
            reflectMethod.invoke(null, ModuleRegistrar.instance());

            mod_BlockHelper.log.info(String.format("Success in registering %s", modname));

        } catch (ClassNotFoundException e) {
            mod_BlockHelper.log.warning(String.format("Could not find class %s", className));
        } catch (NoSuchMethodException e) {
            mod_BlockHelper.log.warning(String.format("Could not find method %s", methodName));
        } catch (Exception e) {
            mod_BlockHelper.log.warning(String.format("Exception while trying to access the method : %s",
                    e));
        }
    }

}
