package mcp.mobius.waila.client;

import mcp.mobius.waila.addons.nei.NEIHandler;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.screens.config.ScreenConfig;
import mcp.mobius.waila.overlay.NEIOverlayRenderer;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.config.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.src.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.input.Keyboard;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class ConfigKeyHandler {

    public final KeyBinding keyCfg;
    public final KeyBinding keyShow;
    public final KeyBinding keyLiquid;
    public final KeyBinding keyRecipe;
    public final KeyBinding keyUsage;
    public final KeyBinding keyLLOverlay;
    public final KeyBinding keyCBOverlay;

    public ConfigKeyHandler(mod_BlockHelper mod) {
        ModLoader.RegisterKey(mod, keyCfg =
                new KeyBinding(Constants.BIND_WAILA_CFG, Keyboard.KEY_NUMPAD0), false);
        ModLoader.RegisterKey(mod, keyShow =
                new KeyBinding(Constants.BIND_WAILA_SHOW, Keyboard.KEY_NUMPAD1), false);
        ModLoader.RegisterKey(mod, keyLiquid =
                new KeyBinding(Constants.BIND_WAILA_LIQUID, Keyboard.KEY_NUMPAD2), false);
        ModLoader.RegisterKey(mod, keyRecipe =
                new KeyBinding(Constants.BIND_WAILA_RECIPE, Keyboard.KEY_NUMPAD3), false);
        ModLoader.RegisterKey(mod, keyUsage =
                new KeyBinding(Constants.BIND_WAILA_USAGE, Keyboard.KEY_NUMPAD4), false);
        ModLoader.RegisterKey(mod, keyLLOverlay =
                new KeyBinding(Constants.BIND_WAILA_LLOVERLAY, Keyboard.KEY_F7), false);
        ModLoader.RegisterKey(mod, keyCBOverlay =
                new KeyBinding(Constants.BIND_WAILA_CBOVERLAY, Keyboard.KEY_F9), false);
    }

    public void onTickInGame(Minecraft mc) {
        if (mc.theWorld == null || mc.thePlayer == null || mc.currentScreen != null) return;

        if (keyCfg.isPressed())
            mc.displayGuiScreen(new ScreenConfig(null));

        if (keyShow.isPressed()) {
            if (PluginConfig.instance().get(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_MODE, true)) {
                boolean status = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                        Constants.CFG_WAILA_SHOW, true);
                PluginConfig.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, !status);
                mc.thePlayer.addChatMessage(WHITE + ITALIC +
                                            I18n.translate(status ? "client.msg.now_hidden" : "client.msg.now_shown"));
            } else {
                PluginConfig.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, true);
            }
        }

        if (keyLiquid.isPressed()) {
            boolean status = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                    Constants.CFG_WAILA_LIQUID, false);
            PluginConfig.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, !status);
            mc.thePlayer.addChatMessage(WHITE + ITALIC + I18n.translate(
                    status ? "client.msg.liquid_now_hidden" : "client.msg.liquid_now_shown"));
        }

        if (keyRecipe.isPressed()) {
            if (ModLoader.isModLoaded("mod_NotEnoughItems")) {
                try {
                    NEIHandler.openRecipeGUI(true);
                } catch (Throwable ignored) {
                }
            }
        }

        if (keyUsage.isPressed()) {
            if (ModLoader.isModLoaded("mod_NotEnoughItems")) {
                try {
                    NEIHandler.openRecipeGUI(false);
                } catch (Throwable ignored) {
                }
            }
        }

        if (keyLLOverlay.isPressed()) {
            try {
                NEIOverlayRenderer.renderMobSpawnOverlay = !NEIOverlayRenderer.renderMobSpawnOverlay;
            } catch (Throwable ignored) {
            }
        }

        if (keyCBOverlay.isPressed()) {
            try {
                NEIOverlayRenderer.renderChunkBounds = (NEIOverlayRenderer.renderChunkBounds + 1) % 3;
            } catch (Throwable ignored) {
            }
        }
    }

}
