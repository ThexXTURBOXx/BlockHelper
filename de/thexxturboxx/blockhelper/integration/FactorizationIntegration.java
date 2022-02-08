package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import factorization.common.TileEntityCommon;
import net.minecraft.item.ItemStack;

public class FactorizationIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "factorization.common.TileEntityCommon")) {
            return new ItemStack(state.block, 1, ((TileEntityCommon) state.te).getFactoryType().md);
        }
        return super.getItemStack(state);
    }

    @Override
    public String getMod(BlockHelperBlockState state) {
        if (iof(state.te, "factorization.common.TileEntityCommon")) {
            return "Factorization";
        }
        return super.getMod(state);
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.factorizationIntegration;
    }

}
