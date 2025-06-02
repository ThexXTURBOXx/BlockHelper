package mcp.mobius.waila.addons.hmi;

import java.lang.reflect.Method;
import java.util.List;
import mcp.mobius.waila.overlay.RayTracing;
import mcp.mobius.waila.utils.AccessHelper;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

public final class HMIFabricHandler {

    private HMIFabricHandler() {
        throw new UnsupportedOperationException();
    }

    public static boolean firstInventory = true;

    private static Method HowManyItemsClient_pushRecipe = null;

    public static boolean openRecipeGUI(boolean recipe) {
        Minecraft mc = ModLoader.getMinecraftInstance();

        if (RayTracing.instance().getTarget() == null) return true;

        List<ItemStack> stacks = RayTracing.instance().getIdentifierItems();
        if (stacks.isEmpty()) return true;

        if (firstInventory) {
            try {
                Class<?> HowManyItemsClient = AccessHelper.getClass("net.glasslauncher.hmifabric.HowManyItemsClient");
                try {
                    HowManyItemsClient_pushRecipe = AccessHelper.getMethod(HowManyItemsClient,
                            new Class[]{GuiScreen.class, ItemStack.class, boolean.class},
                            "pushRecipe");
                } catch (Throwable t) {
                    WailaExceptionHandler.handleErr(t, "HMIFabricHandler#openRecipeGUI", null);
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
            if (HowManyItemsClient_pushRecipe != null)
                HowManyItemsClient_pushRecipe.invoke(null, inv, stacks.get(0).copy(), !recipe);
            return true;
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "HMIFabricHandler#openRecipeGUI", null);
        }

        return false;
    }

}
