package mcp.mobius.waila.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.screens.config.ScreenConfig;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.config.Configuration;
import net.minecraft.client.Minecraft;
import net.minecraft.src.KeyBinding;
import org.lwjgl.input.Keyboard;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class ConfigKeyHandler {

    public final BlockHelperKeyBinding keyCfg;
    public final BlockHelperKeyBinding keyShow;
    public final BlockHelperKeyBinding keyLiquid;

    private final List<BlockHelperKeyBinding> keyBindings = new ArrayList<BlockHelperKeyBinding>();
    private boolean firstTick = true;

    public ConfigKeyHandler() {
        keyBindings.add(keyCfg =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_CFG, Keyboard.KEY_NUMPAD0));
        keyBindings.add(keyShow =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_SHOW, Keyboard.KEY_NUMPAD1));
        keyBindings.add(keyLiquid =
                new BlockHelperKeyBinding(Constants.BIND_WAILA_LIQUID, Keyboard.KEY_NUMPAD2));
    }

    public void onTickInGame(Minecraft mc) {
        if (firstTick) {
            List<KeyBinding> mcKeyBindings = new ArrayList<KeyBinding>(Arrays.asList(mc.gameSettings.keyBindings));
            mcKeyBindings.addAll(keyBindings);
            mc.gameSettings.keyBindings = mcKeyBindings.toArray(new KeyBinding[0]);
            firstTick = false;
        }

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
    }

}
