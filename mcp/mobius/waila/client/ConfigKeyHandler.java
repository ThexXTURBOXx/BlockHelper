package mcp.mobius.waila.client;

import cpw.mods.fml.client.registry.KeyBindingRegistry.KeyHandler;

import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import java.util.EnumSet;
import mcp.mobius.waila.mod_Waila;
import mcp.mobius.waila.overlay.WailaTickHandler;
import net.minecraft.src.ModLoader;
import net.minecraftforge.event.ForgeSubscribe;
import org.lwjgl.input.Keyboard;

import cpw.mods.fml.common.Loader;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.gui.screens.config.ScreenConfig;
import mcp.mobius.waila.utils.Constants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.common.Configuration;

public class ConfigKeyHandler implements ITickHandler {

    public static KeyBinding key_cfg;
    public static KeyBinding key_show;
    public static KeyBinding key_liquid;
    public static KeyBinding key_recipe;
    public static KeyBinding key_usage;

	public static void init(mod_Waila mod) {
		ModLoader.registerKey(mod, key_cfg    = new KeyBinding(Constants.BIND_WAILA_CFG,     Keyboard.KEY_NUMPAD0), false);
        ModLoader.registerKey(mod, key_show   = new KeyBinding(Constants.BIND_WAILA_SHOW,    Keyboard.KEY_NUMPAD1), false);
        ModLoader.registerKey(mod, key_liquid = new KeyBinding(Constants.BIND_WAILA_LIQUID,  Keyboard.KEY_NUMPAD2), false);
        ModLoader.registerKey(mod, key_recipe = new KeyBinding(Constants.BIND_WAILA_RECIPE,  Keyboard.KEY_NUMPAD3), false);
        ModLoader.registerKey(mod, key_usage  = new KeyBinding(Constants.BIND_WAILA_USAGE,   Keyboard.KEY_NUMPAD4), false);
        TickRegistry.registerTickHandler(new ConfigKeyHandler(), Side.CLIENT);
	}

    @Override
    public void tickStart(EnumSet<TickType> type, Object... tickData) {
        Minecraft mc = Minecraft.getMinecraft();

        if (key_cfg.isPressed()){
            if(mc.currentScreen == null)
                mc.displayGuiScreen(new ScreenConfig(mc.currentScreen));
        }

        if (mc.currentScreen != null)
            return;

        if (key_show.isPressed() && ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_MODE, false)){
            boolean status = ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, true);
            ConfigHandler.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, !status);
        }

        if (key_show.isPressed() && !ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_MODE, false)){
            ConfigHandler.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_SHOW, true);
        }

        if (key_liquid.isPressed()){
            boolean status = ConfigHandler.instance().getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, true);
            ConfigHandler.instance().setConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_LIQUID, !status);
        }

        if (key_recipe.isPressed()){
            if (Loader.isModLoaded("NotEnoughItems")){
                try{
                    Class.forName("mcp.mobius.waila.handlers.nei.NEIHandler").getDeclaredMethod("openRecipeGUI", boolean.class).invoke(null, true);
                } catch (Exception e){}
            }
        }

        if (key_usage.isPressed()){
            if (Loader.isModLoaded("NotEnoughItems")){
                try{
                    Class.forName("mcp.mobius.waila.handlers.nei.NEIHandler").getDeclaredMethod("openRecipeGUI", boolean.class).invoke(null, false);
                } catch (Exception e){}
            }
        }
    }

    @Override
    public void tickEnd(EnumSet<TickType> type, Object... tickData) {
    }

    @Override
	public EnumSet<TickType> ticks() {
        return EnumSet.of(TickType.RENDER, TickType.CLIENT);
	}

	@Override
	public String getLabel() {
        return "Waila Key Handler";
	}

}
