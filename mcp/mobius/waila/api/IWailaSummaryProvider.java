package mcp.mobius.waila.api;

import java.util.Map;
import net.minecraft.item.ItemStack;

public interface IWailaSummaryProvider {
    /* This interface is used to control the display data in the description screen */

    /* BASIC TOOLS & ITEMS DATA */
    //EnumToolMaterial getMaterial(ItemStack stack);
    //String getMaterialName(ItemStack stack);
    //String getEffectiveBlock(ItemStack stack);
    //int getHarvestLevel(ItemStack stack);
    //float getEfficiencyOnProperMaterial(ItemStack stack);
    //int getEnchantability(ItemStack stack);
    //int getDamageVsEntity(ItemStack stack);
    //int getDurability(ItemStack stack);

    Map<String, String> getSummary(ItemStack stack, Map<String, String> currentSummary,
                                   IWailaConfigHandler config);
}
