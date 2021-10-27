package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.src.ItemStack;

public class PamIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "TileEntityPamCrop")) {
            int[] seedDrops = getDeclaredField(getClass("TileEntityPamCrop"), state.te, "SeedDrops");
            Integer cropID = getDeclaredField(getClass("TileEntityPamCrop"), state.te, "CropID");
            if (seedDrops != null && cropID != null) {
                return new ItemStack(seedDrops[cropID], 1, 0);
            }
        }
        return super.getItemStack(state);
    }

}
