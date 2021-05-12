package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperState;
import factorization.common.TileEntityCommon;
import net.minecraft.item.ItemStack;

public class FactorizationIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperState state) {
        if (iof(state.te, "factorization.common.TileEntityCommon")) {
            return new ItemStack(state.block, 1, ((TileEntityCommon) state.te).getFactoryType().md);
        }
        return super.getItemStack(state);
    }

    @Override
    public String getMod(BlockHelperState state) {
        if (iof(state.te, "factorization.common.TileEntityCommon")) {
            return "Factorization";
        }
        return super.getMod(state);
    }

}
