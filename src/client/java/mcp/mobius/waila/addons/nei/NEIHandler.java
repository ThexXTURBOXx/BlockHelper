package mcp.mobius.waila.addons.nei;

import codechicken.nei.API;
import codechicken.nei.GuiCraftingRecipe;
import codechicken.nei.GuiUsageRecipe;
import java.util.List;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.overlay.RayTracing;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.I18n;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;
import org.lwjgl.input.Keyboard;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class NEIHandler {

    private NEIHandler() {
        throw new UnsupportedOperationException();
    }

    public static void register() {
        API.addTooltipHandler(TooltipHandlerWaila.INSTANCE);
        WailaRegistrar.instance().addConfig("Not Enough Items", "nei.modtooltips");

        try {
            API.addInputHandler(HandlerEnchants.INSTANCE);
            API.addKeyBind(Constants.BIND_SCREEN_ENCH, I18n.translate("nei.options.keys.showenchant"), Keyboard.KEY_I);
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "NEIHandler#register - Enchantment Handler", null);
        }
    }

    public static void openRecipeGUI(boolean recipe) {
        Minecraft mc = ModLoader.getMinecraftInstance();

        if (RayTracing.instance().getTarget() == null) return;

        List<ItemStack> stacks = RayTracing.instance().getIdentifierItems();
        if (stacks.isEmpty()) return;

        GuiInventory inv = new GuiInventory(mc.thePlayer);
        mc.displayGuiScreen(inv);

        try {
            if (recipe) {
                if (GuiCraftingRecipe.hasRecipe(stacks.get(0).copy())) {
                    mc.displayGuiScreen(new GuiCraftingRecipe(stacks.get(0).copy(), inv));
                } else {
                    ItemStack target = stacks.get(0).copy();
                    target.setItemDamage(0);
                    if (GuiCraftingRecipe.hasRecipe(target)) {
                        mc.displayGuiScreen(new GuiCraftingRecipe(target, inv));
                    } else {
                        mc.thePlayer.addChatMessage(WHITE + ITALIC + I18n.translate("client.msg.norecipe"));
                        mc.displayGuiScreen(null);
                        mc.setIngameFocus();
                    }
                }
            } else {
                if (GuiUsageRecipe.hasRecipe(stacks.get(0).copy())) {
                    mc.displayGuiScreen(new GuiUsageRecipe(stacks.get(0).copy(), inv));
                } else {
                    ItemStack target = stacks.get(0).copy();
                    target.setItemDamage(0);
                    if (GuiUsageRecipe.hasRecipe(target)) {
                        mc.displayGuiScreen(new GuiUsageRecipe(target, inv));
                    } else {
                        mc.thePlayer.addChatMessage(WHITE + ITALIC + I18n.translate("client.msg.nousage"));
                        mc.displayGuiScreen(null);
                        mc.setIngameFocus();
                    }
                }
            }
        } catch (Throwable t) {
            if (recipe) {
                if (!GuiCraftingRecipe.openRecipeGui("item", stacks.get(0).copy())) {
                    ItemStack target = stacks.get(0).copy();
                    target.setItemDamage(0);
                    if (!GuiCraftingRecipe.openRecipeGui("item", target)) {
                        mc.thePlayer.addChatMessage(WHITE + ITALIC + I18n.translate("client.msg.norecipe"));
                        mc.displayGuiScreen(null);
                        mc.setIngameFocus();
                    }
                }
            } else {
                if (!GuiUsageRecipe.openRecipeGui("item", stacks.get(0).copy())) {
                    ItemStack target = stacks.get(0).copy();
                    target.setItemDamage(0);
                    if (!GuiUsageRecipe.openRecipeGui("item", target)) {
                        mc.thePlayer.addChatMessage(WHITE + ITALIC + I18n.translate("client.msg.nousage"));
                        mc.displayGuiScreen(null);
                        mc.setIngameFocus();
                    }
                }
            }
        }
    }

}
