package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class TEIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(Block block, TileEntity te, int id, int meta) {
        if (iof(te, "thermalexpansion.transport.tileentity.TileConduitLiquid")) {
            return new ItemStack(block, 1, 4096);
        }
        return super.getItemStack(block, te, id, meta);
    }

}
