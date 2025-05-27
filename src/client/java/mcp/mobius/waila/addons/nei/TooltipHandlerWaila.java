package mcp.mobius.waila.addons.nei;

import codechicken.nei.IHandleTooltip;
import java.util.List;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.src.ItemStack;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.ITALIC;

public final class TooltipHandlerWaila implements IHandleTooltip {

    public static final IHandleTooltip INSTANCE = new TooltipHandlerWaila();

    private TooltipHandlerWaila() {
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List handleTooltip(ItemStack stack, List list) {
        if (!PluginConfig.instance().get("nei.modtooltips")) return list;
        String canonicalName = ModIdentification.identifyMod(stack);
        if (canonicalName != null && !canonicalName.isEmpty())
            list.add(BLUE + ITALIC + canonicalName);
        return list;
    }

}
