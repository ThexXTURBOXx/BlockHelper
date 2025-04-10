package mcp.mobius.waila.handlers.nei;

import codechicken.nei.forge.IContainerTooltipHandler;
import java.util.Collections;
import java.util.List;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class TooltipHandlerWaila implements IContainerTooltipHandler {

    @Override
    public List<String> handleTooltipFirst(GuiContainer guiContainer, int i, int i1, List<String> list) {
        return Collections.emptyList();
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer guiContainer, ItemStack itemStack, List<String> list) {
        String canonicalName = ModIdentification.nameFromStack(itemStack);
        if (canonicalName != null && !canonicalName.isEmpty())
            list.add("\u00a79\u00a7o" + canonicalName);
        return list;
    }

}
