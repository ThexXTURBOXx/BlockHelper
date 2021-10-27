package de.thexxturboxx.blockhelper.integration.nei;

import codechicken.nei.API;
import codechicken.nei.IHandleTooltip;
import java.util.List;
import net.minecraft.src.ItemStack;
import net.minecraft.src.ModLoader;

public class NEIIntegration implements IHandleTooltip {

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List handleTooltip(ItemStack stack, List list) {
        if (ModLoader.getMinecraftInstance().thePlayer.inventory.getItemStack() != null) {
            return list;
        }

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
