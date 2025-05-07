package mcp.mobius.waila.addons.nei;

import codechicken.nei.forge.IContainerTooltipHandler;
import java.util.List;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public final class TooltipHandlerWaila implements IContainerTooltipHandler {

    public static final IContainerTooltipHandler INSTANCE = new TooltipHandlerWaila();

    private TooltipHandlerWaila() {
    }

    @Override
    public List handleTooltipFirst(GuiContainer guiContainer, int i, int i1, List list) {
        return list;
    }

    @Override
    public List handleItemTooltip(GuiContainer guiContainer, ItemStack itemStack, List list) {
        if (!PluginConfig.instance().get("nei.modtooltips")) return list;
        String canonicalName = ModIdentification.nameFromStack(itemStack);
        if (canonicalName != null && !canonicalName.isEmpty())
            list.add(BLUE + ITALIC + canonicalName);
        return list;
    }

}
