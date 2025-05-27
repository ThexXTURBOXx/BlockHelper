package mcp.mobius.waila.addons.nei;

import codechicken.nei.API;
import codechicken.nei.GuiCraftingRecipe;
import codechicken.nei.GuiUsageRecipe;
import java.util.List;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.overlay.RayTracing;
import mcp.mobius.waila.utils.I18n;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiInventory;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

import static mcp.mobius.waila.api.SpecialChars.ITALIC;
import static mcp.mobius.waila.api.SpecialChars.WHITE;

public final class NEIHandler {

    private NEIHandler() {
        throw new UnsupportedOperationException();
    }

    public static void register() {
        API.addTooltipHandler(TooltipHandlerWaila.INSTANCE);
        WailaRegistrar.instance().addConfig("Not Enough Items", "nei.modtooltips");
    }

    public static void openRecipeGUI(boolean recipe) {
        Minecraft mc = ModLoader.getMinecraftInstance();

        if (RayTracing.instance().getTarget() == null) return;

        List<ItemStack> stacks = RayTracing.instance().getIdentifierItems();
        if (stacks.isEmpty()) return;

        GuiInventory inv = new GuiInventory(mc.thePlayer);
        mc.displayGuiScreen(inv);

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
    }

}
