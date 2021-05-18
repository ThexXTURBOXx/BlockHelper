package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.src.ItemStack;

public class TEIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "thermalexpansion.transport.tileentity.TileConduitLiquid")) {
            return new ItemStack(state.block, 1, 4096);
        }
        return super.getItemStack(state);
    }

}
