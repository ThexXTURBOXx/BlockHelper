package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import gregtechmod.api.metatileentity.BaseMetaTileEntity;
import net.minecraft.item.ItemStack;

public class GregTechIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "gregtechmod.api.metatileentity.BaseMetaTileEntity")) {
            BaseMetaTileEntity bmte = (BaseMetaTileEntity) state.te;
            return new ItemStack(state.id, 1, bmte.getMetaTileID());
        }
        return super.getItemStack(state);
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.gregTechIntegration;
    }

}
