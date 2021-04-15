package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import net.meteor.common.MeteorsMod;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class MeteorsIntegration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(Block b, int id, int meta, InfoHolder info) {
        if (iof(b, "net.meteor.common.BlockMeteorShieldTorch")) {
            info.add("State: "
                    + (id == MeteorsMod.torchMeteorShieldActive.blockID ? "Protected" : "Unprotected"));
        }
    }

    @Override
    public void addInformation(TileEntity te, int id, int meta, InfoHolder info) {
        if (iof(te, "net.meteor.common.TileEntityMeteorShield")) {
            if (meta == 0) {
                info.add("State: Charging");
            } else {
                info.add("Radius: " + meta * 4 + "x" + meta * 4 + " Chunks");
            }
        }
    }

    @Override
    public ItemStack getItemStack(Block block, TileEntity te, int id, int meta) {
        if (iof(block, "net.meteor.common.BlockMeteorShieldTorch")) {
            return new ItemStack(block.idDropped(0, null, 0), 1, meta);
        }
        return super.getItemStack(block, te, id, meta);
    }

}
