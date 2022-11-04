package de.thexxturboxx.blockhelper.integration;

import com.eloraam.redpower.RedPowerWorld;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RP2Integration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.block, "com.eloraam.redpower.world.BlockCustomCrops")) {
            return new ItemStack(
                    BlockHelperInfoProvider.<Integer>getField(Item.class, RedPowerWorld.itemSeeds, "cj"), 1, 0);
        }
        return super.getItemStack(state);
    }

}
