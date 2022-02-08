package de.thexxturboxx.blockhelper.integration.nei;

import codechicken.nei.forge.GuiContainerManager;
import codechicken.nei.forge.IContainerTooltipHandler;
import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.item.ItemStack;

public class NEIIntegration implements IContainerTooltipHandler {

    @Override
    @SuppressWarnings("rawtypes")
    public List handleTooltipFirst(GuiContainer guiContainer, int i, int i1, List list) {
        return list;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List handleItemTooltip(GuiContainer guiContainer, ItemStack itemStack, List list) {
        if (!BlockHelperCommonProxy.neiIntegration) {
            return list;
        }

        String mod = ModIdentifier.identifyMod(itemStack);
        if (mod != null) {
            list.add("§9§o" + mod);
        }
        return list;
    }

    public static void register() {
        GuiContainerManager.addTooltipHandler(new NEIIntegration());
    }

}
