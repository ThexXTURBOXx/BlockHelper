package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
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
        if (iof(state.te, "TileEntityPamSimpleCrop")) {
            int[] seedDrops = getDeclaredField(getClass("TileEntityPamSimpleCrop"), state.te, "SeedDrops");
            Integer cropID = getDeclaredField(getClass("TileEntityPamSimpleCrop"), state.te, "SimpleCropID");
            if (seedDrops != null && cropID != null) {
                return new ItemStack(seedDrops[cropID], 1, 0);
            }
        }
        if (iof(state.te, "TileEntityPamFlowerCrop")) {
            int[] seedDrops = getDeclaredField(getClass("TileEntityPamFlowerCrop"), state.te, "SeedDrops");
            Integer cropID = getDeclaredField(getClass("TileEntityPamFlowerCrop"), state.te, "FlowerCropID");
            if (seedDrops != null && cropID != null) {
                return new ItemStack(seedDrops[cropID], 1, cropID > 13 ? 0 : cropID);
            }
        }
        if (iof(state.block, "BlockPamCactusFruitCrop")) {
            return new ItemStack(state.block.idDropped(7, null, 0), 1, 0);
        }
        return super.getItemStack(state);
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.pamIntegration;
    }

}
