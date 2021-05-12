package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.BlockHelperState;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import net.meteor.common.MeteorsMod;
import net.minecraft.item.ItemStack;

public class MeteorsIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperState state, InfoHolder info) {
        if (iof(state.block, "net.meteor.common.block.BlockMeteorShieldTorch")) {
            info.add("State: "
                    + (state.id == MeteorsMod.torchMeteorShieldActive.blockID ? "Protected" : "Unprotected"));
        }
        if (iof(state.te, "net.meteor.common.tileentity.TileEntityMeteorShield")) {
            if (state.meta == 0) {
                info.add("State: Charging");
            } else {
                info.add("Radius: " + state.meta * 4 + "x" + state.meta * 4 + " Chunks");
            }
        }
    }

    @Override
    public ItemStack getItemStack(BlockHelperState state) {
        if (iof(state.block, "net.meteor.common.block.BlockMeteorShieldTorch")) {
            return new ItemStack(state.block.idDropped(0, null, 0), 1, state.meta);
        }
        return super.getItemStack(state);
    }

}
