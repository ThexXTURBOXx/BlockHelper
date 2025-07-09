package mcp.mobius.waila.client;

import mcp.mobius.waila.addons.ami.AMIHandler;
import mcp.mobius.waila.addons.hmi.HMIFabricHandler;
import mcp.mobius.waila.addons.hmi.HMIHandler;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.screens.config.ScreenConfig;
import mcp.mobius.waila.overlay.NEIOverlayRenderer;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.config.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.input.Keyboard;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class ConfigKeyHandler {

    public final BlockHelperKeyBinding keyCfg;
    public final BlockHelperKeyBinding keyShow;
    public final BlockHelperKeyBinding keyLiquid;
    public final BlockHelperKeyBinding keyRecipe;
    public final BlockHelperKeyBinding keyUsage;
    public final BlockHelperKeyBinding keyLLOverlay;
    public final BlockHelperKeyBinding keyCBOverlay;

    public ConfigKeyHandler(mod_BlockHelper mod) {
        ModLoader.RegisterKey(mod, keyCfg =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_CFG, Keyboard.KEY_NUMPAD0), false);
        ModLoader.RegisterKey(mod, keyShow =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_SHOW, Keyboard.KEY_NUMPAD1), false);
        ModLoader.RegisterKey(mod, keyLiquid =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_LIQUID, Keyboard.KEY_NUMPAD2), false);
        ModLoader.RegisterKey(mod, keyRecipe =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_RECIPE, Keyboard.KEY_NUMPAD3), false);
        ModLoader.RegisterKey(mod, keyUsage =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_USAGE, Keyboard.KEY_NUMPAD4), false);
        ModLoader.RegisterKey(mod, keyLLOverlay =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_LLOVERLAY, Keyboard.KEY_F7), false);
        ModLoader.RegisterKey(mod, keyCBOverlay =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_CBOVERLAY, Keyboard.KEY_F9), false);
    }

    public void onTickInGame(Minecraft mc) {
        BlockHelperKeyBinding.onTick();

        if (mc.theWorld == null || mc.thePlayer == null || mc.currentScreen != null) return;

        if (keyCfg.isClicked())
            mc.displayGuiScreen(new ScreenConfig(null));

        if (keyShow.isClicked()) {
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

        if (keyLiquid.isClicked()) {
            boolean status = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                    Constants.CFG_WAILA_LIQUID, false);
            PluginConfig.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, !status);
            mc.thePlayer.addChatMessage(WHITE + ITALIC + I18n.translate(
                    status ? "client.msg.liquid_now_hidden" : "client.msg.liquid_now_shown"));
        }

        if (keyRecipe.isClicked()) {
            boolean handled = false;

            try {
                handled = AMIHandler.openRecipeGUI(true);
            } catch (Throwable ignored) {
            }

            if (!handled) {
                try {
                    handled = HMIHandler.openRecipeGUI(true);
                } catch (Throwable ignored) {
                }
            }

            if (!handled) {
                try {
                    handled = HMIFabricHandler.openRecipeGUI(true);
                } catch (Throwable ignored) {
                }
            }
        }

        if (keyUsage.isClicked()) {
            boolean handled = false;

            try {
                handled = AMIHandler.openRecipeGUI(false);
            } catch (Throwable ignored) {
            }

            if (!handled) {
                try {
                    handled = HMIHandler.openRecipeGUI(false);
                } catch (Throwable ignored) {
                }
            }

            if (!handled) {
                try {
                    handled = HMIFabricHandler.openRecipeGUI(false);
                } catch (Throwable ignored) {
                }
            }
        }

        if (keyLLOverlay.isClicked()) {
            try {
                NEIOverlayRenderer.renderMobSpawnOverlay = !NEIOverlayRenderer.renderMobSpawnOverlay;
            } catch (Throwable ignored) {
            }
        }

        if (keyCBOverlay.isClicked()) {
            try {
                NEIOverlayRenderer.renderChunkBounds = (NEIOverlayRenderer.renderChunkBounds + 1) % 3;
            } catch (Throwable ignored) {
            }
        }
    }

}
