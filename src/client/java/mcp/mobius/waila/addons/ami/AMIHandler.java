package mcp.mobius.waila.addons.ami;

import java.lang.reflect.Method;
import java.util.List;
import mcp.mobius.waila.overlay.RayTracing;
import mcp.mobius.waila.utils.AccessHelper;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

public final class AMIHandler {

    private AMIHandler() {
        throw new UnsupportedOperationException();
    }

    public static boolean firstInventory = true;

    private static Method Focus_create = null;

    private static Object OverlayScreen_INSTANCE = null;
    private static Method OverlayScreen_showRecipe = null;
    private static Method OverlayScreen_showUses = null;

    public static boolean openRecipeGUI(boolean recipe) {
        Minecraft mc = ModLoader.getMinecraftInstance();

        if (RayTracing.instance().getTarget() == null) return true;

        List<ItemStack> stacks = RayTracing.instance().getIdentifierItems();
        if (stacks.isEmpty()) return true;

        if (firstInventory) {
            try {
                Class<?> Focus = AccessHelper.getClass(
                        "net.glasslauncher.mods.alwaysmoreitems.recipe.Focus");
                Class<?> OverlayScreen = AccessHelper.getClass(
                        "net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen");
                try {
                    Focus_create = AccessHelper.getMethod(Focus, new Class[]{Object.class}, "create");

                    OverlayScreen_INSTANCE = AccessHelper.getField(OverlayScreen, "INSTANCE").get(null);
                    OverlayScreen_showRecipe = AccessHelper.getMethod(OverlayScreen, new Class[]{Focus},
                            "showRecipe");
                    OverlayScreen_showUses = AccessHelper.getMethod(OverlayScreen, new Class[]{Focus},
                            "showUses");
                } catch (Throwable t) {
                    WailaExceptionHandler.handleErr(t, "AMIHandler#openRecipeGUI", null);
                    return false;
                }
            } catch (Throwable ignored) {
                return false;
            }
            firstInventory = false;
        }

        GuiInventory inv = new GuiInventory(mc.thePlayer);
        mc.displayGuiScreen(inv);

        try {
            if (recipe) {
                if (OverlayScreen_showRecipe != null)
                    OverlayScreen_showRecipe.invoke(OverlayScreen_INSTANCE,
                            Focus_create.invoke(null, stacks.get(0).copy()));
            } else {
                if (OverlayScreen_showUses != null)
                    OverlayScreen_showUses.invoke(OverlayScreen_INSTANCE,
                            Focus_create.invoke(null, stacks.get(0).copy()));
            }
            return true;
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "AMIHandler#openRecipeGUI", null);
        }

        return false;
    }

}
