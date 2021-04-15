package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.src.Block;
import net.minecraft.src.ItemStack;
import net.minecraft.src.TileEntity;

public class TEIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(Block block, TileEntity te, int id, int meta) {
        if (iof(te, "thermalexpansion.transport.tileentity.TileConduitLiquid")) {
            return new ItemStack(block, 1, 4096);
        }
        return super.getItemStack(block, te, id, meta);
    }

}
