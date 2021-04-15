package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import factorization.common.TileEntityCommon;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class FactorizationIntegration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(Block block, TileEntity te, int id, int meta) {
        if (iof(te, "factorization.common.TileEntityCommon")) {
            return new ItemStack(block, 1, ((TileEntityCommon) te).getFactoryType().md);
        }
        return super.getItemStack(block, te, id, meta);
    }

    @Override
    public String getMod(Block block, TileEntity te, int id, int meta) {
        if (iof(te, "factorization.common.TileEntityCommon")) {
            return "Factorization";
        }
        return super.getMod(block, te, id, meta);
    }

}
