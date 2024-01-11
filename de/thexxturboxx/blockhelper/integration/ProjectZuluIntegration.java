package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import de.thexxturboxx.blockhelper.i18n.I18n;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ProjectZuluIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.te, "projectzulu.common.blocks.TileEntityUniversalFlowerPot")) {
            IInventory pot = (IInventory) state.te;
            ItemStack flower = pot.getStackInSlot(0);
            if (flower != null)
                info.add(I18n.format(state.translator, "flower", flower.getDisplayName()));
        }
    }

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.te, "projectzulu.common.blocks.TileEntityUniversalFlowerPot")) {
            return new ItemStack(Item.itemsList[Item.flowerPot.itemID], 1);
        }

        return super.getItemStack(state);
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.projectZuluIntegration;
    }

}
