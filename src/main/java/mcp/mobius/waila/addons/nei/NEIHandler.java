package mcp.mobius.waila.addons.nei;

import codechicken.nei.api.API;
import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import java.util.List;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.overlay.RayTracing;
import mcp.mobius.waila.utils.Constants;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.ItemStack;
import org.lwjgl.input.Keyboard;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class NEIHandler {

    private NEIHandler() {
        throw new UnsupportedOperationException();
    }

    public static void register() {
        GuiContainerManager.addTooltipHandler(TooltipHandlerWaila.INSTANCE);
        WailaRegistrar.instance().addConfig("Not Enough Items", "nei.modtooltips");

        GuiContainerManager.addInputHandler(HandlerEnchants.INSTANCE);
        API.addKeyBind(Constants.BIND_SCREEN_ENCH, I18n.translate("nei.options.keys.showenchant"), Keyboard.KEY_I);
    }

    public static void openRecipeGUI(boolean recipe) {
        Minecraft mc = Minecraft.getMinecraft();

        if (RayTracing.instance().getTarget() == null) return;

        List<ItemStack> stacks = RayTracing.instance().getIdentifierItems();
        if (stacks.isEmpty()) return;

        GuiInventory inv = new GuiInventory(mc.thePlayer);
        mc.displayGuiScreen(inv);

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
