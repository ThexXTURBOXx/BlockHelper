package de.thexxturboxx.blockhelper.integration.nei;

import codechicken.nei.API;
import codechicken.nei.IHandleTooltip;
import java.util.List;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.ItemStack;

public class NEIIntegration implements IHandleTooltip {

    @Override
    @SuppressWarnings("rawtypes")
    public boolean canHandle(Class clazz) {
        return true;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List handleTooltip(GuiContainer guiContainer, List list) {
        String mod = ModIdentifier.identifyMod(guiContainer.getStackMouseOver());
        if (mod != null) {
            list.add("\u00a79\u00a7o" + mod);
        }
        return list;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List handleTooltip(ItemStack stack, List list) {
        String mod = ModIdentifier.identifyMod(stack);
        if (mod != null) {
            list.add("\u00a79\u00a7o" + mod);
        }
        return list;
    }

    public static void register() {
        API.addTooltipHandler(new NEIIntegration());
    }

}
