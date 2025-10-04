package mcp.mobius.waila.client;

import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.screens.config.ScreenConfig;
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

    public static boolean showAdvancedBody = false;
    public static int advancedBodyKey = 0;

    public final BlockHelperKeyBinding keyCfg;
    public final BlockHelperKeyBinding keyShow;
    public final BlockHelperKeyBinding keyLiquid;
    public final BlockHelperKeyBinding keyShowAdvanced;

    public ConfigKeyHandler(mod_BlockHelper mod) {
        ModLoader.RegisterKey(mod, keyCfg =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_CFG, Keyboard.KEY_NUMPAD0), false);
        ModLoader.RegisterKey(mod, keyShow =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_SHOW, Keyboard.KEY_NUMPAD1), false);
        ModLoader.RegisterKey(mod, keyLiquid =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_LIQUID, Keyboard.KEY_NUMPAD2), false);
        ModLoader.RegisterKey(mod, keyShowAdvanced =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_SHOW_ADVANCED, Keyboard.KEY_LCONTROL), true);
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
                mc.ingameGUI.addChatMessage(WHITE + ITALIC +
                                            I18n.translate(status ? "client.msg.now_hidden" : "client.msg.now_shown"));
            } else {
                PluginConfig.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, true);
            }
        }

        if (keyLiquid.isClicked()) {
            boolean status = PluginConfig.instance().get(Configuration.CATEGORY_GENERAL,
                    Constants.CFG_WAILA_LIQUID, false);
            PluginConfig.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, !status);
            mc.ingameGUI.addChatMessage(WHITE + ITALIC + I18n.translate(
                    status ? "client.msg.liquid_now_hidden" : "client.msg.liquid_now_shown"));
        }

        showAdvancedBody = keyShowAdvanced.isPressed();
        advancedBodyKey = keyShowAdvanced.keyCode;
    }

}
