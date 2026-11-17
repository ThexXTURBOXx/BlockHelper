package mcp.mobius.waila.addons.nilfix;

import de.femtopedia.nilb173fix.api.TooltipHandler;
import java.util.List;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.src.GuiContainer;
import net.minecraft.src.ItemStack;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public final class TooltipHandlerWaila implements TooltipHandler {

    public static final TooltipHandler INSTANCE = new TooltipHandlerWaila();

    private TooltipHandlerWaila() {
    }

    @Override
    public List<String> handleTooltipFirst(GuiContainer guiContainer, int i, int i1, List<String> list) {
        return list;
    }

    @Override
    public List<String> handleItemTooltip(GuiContainer guiContainer, ItemStack itemStack, List<String> list) {
        if (!PluginConfig.instance().get("nilfix.modtooltips")) return list;
        String canonicalName = ModIdentification.identifyMod(itemStack);
        if (canonicalName != null && !canonicalName.isEmpty())
            list.add(BLUE + ITALIC + canonicalName);
        return list;
    }

}
