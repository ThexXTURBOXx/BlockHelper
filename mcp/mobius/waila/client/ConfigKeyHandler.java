package mcp.mobius.waila.client;

import cpw.mods.fml.common.Loader;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.screens.config.ScreenConfig;
import mcp.mobius.waila.mod_BlockHelper;
import mcp.mobius.waila.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraftforge.common.Configuration;
import org.lwjgl.input.Keyboard;

public class ConfigKeyHandler {

    public final KeyBinding keyCfg;
    public final KeyBinding keyShow;
    public final KeyBinding keyLiquid;
    public final KeyBinding keyRecipe;
    public final KeyBinding keyUsage;

    public ConfigKeyHandler(mod_BlockHelper mod) {
        ModLoader.registerKey(mod, keyCfg =
                new KeyBinding(Constants.BIND_WAILA_CFG, Keyboard.KEY_NUMPAD0), false);
        ModLoader.registerKey(mod, keyShow =
                new KeyBinding(Constants.BIND_WAILA_SHOW, Keyboard.KEY_NUMPAD1), false);
        ModLoader.registerKey(mod, keyLiquid =
                new KeyBinding(Constants.BIND_WAILA_LIQUID, Keyboard.KEY_NUMPAD2), false);
        ModLoader.registerKey(mod, keyRecipe =
                new KeyBinding(Constants.BIND_WAILA_RECIPE, Keyboard.KEY_NUMPAD3), false);
        ModLoader.registerKey(mod, keyUsage =
                new KeyBinding(Constants.BIND_WAILA_USAGE, Keyboard.KEY_NUMPAD4), false);
    }

    public void onTickInGame(Minecraft mc) {
        if (keyCfg.isPressed()) {
            if (mc.currentScreen == null)
                mc.displayGuiScreen(new ScreenConfig(null));
        }

        if (mc.currentScreen != null)
            return;

        if (keyShow.isPressed() && PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                Constants.CFG_WAILA_MODE, false)) {
            boolean status = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                    Constants.CFG_WAILA_SHOW, true);
            PluginConfig.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, !status);
        }

        if (keyShow.isPressed() && !PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                Constants.CFG_WAILA_MODE, false)) {
            PluginConfig.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, true);
        }

        if (keyLiquid.isPressed()) {
            boolean status = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                    Constants.CFG_WAILA_LIQUID, true);
            PluginConfig.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, !status);
        }

        if (keyRecipe.isPressed()) {
            if (Loader.isModLoaded("NotEnoughItems")) {
                try {
                    Class.forName("mcp.mobius.waila.handlers.nei.NEIHandler").getDeclaredMethod("openRecipeGUI",
                            boolean.class).invoke(null, true);
                } catch (Throwable ignored) {
                }
            }
        }

        if (keyUsage.isPressed()) {
            if (Loader.isModLoaded("NotEnoughItems")) {
                try {
                    Class.forName("mcp.mobius.waila.handlers.nei.NEIHandler").getDeclaredMethod("openRecipeGUI",
                            boolean.class).invoke(null, false);
                } catch (Throwable ignored) {
                }
            }
        }
    }

}
