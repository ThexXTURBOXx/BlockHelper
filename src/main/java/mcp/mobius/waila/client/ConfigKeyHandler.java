package mcp.mobius.waila.client;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.TickType;
import java.util.EnumSet;
import mcp.mobius.waila.addons.nei.NEIHandler;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.gui.screens.config.ScreenConfig;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.src.ModLoader;
import net.minecraft.src.mod_BlockHelper;
import net.minecraftforge.common.Configuration;
import org.lwjgl.input.Keyboard;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public class ConfigKeyHandler implements ITickHandler {

    public static boolean showAdvancedBody = false;
    public static int advancedBodyKey = 0;

    public final KeyBinding keyCfg;
    public final KeyBinding keyShow;
    public final KeyBinding keyLiquid;
    public final KeyBinding keyRecipe;
    public final KeyBinding keyUsage;
    public final KeyBinding keyShowAdvanced;

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
        ModLoader.registerKey(mod, keyShowAdvanced =
                new KeyBinding(Constants.BIND_WAILA_SHOW_ADVANCED, Keyboard.KEY_LCONTROL), true);
    }

    @Override
    public void tickStart(EnumSet<TickType> enumSet, Object... objects) {
    }

    @Override
    public void tickEnd(EnumSet<TickType> enumSet, Object... objects) {
        Minecraft mc = Minecraft.getMinecraft();
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
            if (Loader.isModLoaded("NotEnoughItems")) {
                try {
                    NEIHandler.openRecipeGUI(true);
                } catch (Throwable ignored) {
                }
            }
        }

        if (keyUsage.isPressed()) {
            if (Loader.isModLoaded("NotEnoughItems")) {
                try {
                    NEIHandler.openRecipeGUI(false);
                } catch (Throwable ignored) {
                }
            }
        }

        showAdvancedBody = keyShowAdvanced.pressed;
        advancedBodyKey = keyShowAdvanced.keyCode;
    }

    @Override
    public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.CLIENT);
    }

    @Override
    public String getLabel() {
        return mod_BlockHelper.MOD_ID + ":ConfigKeyHandler";
    }

}
