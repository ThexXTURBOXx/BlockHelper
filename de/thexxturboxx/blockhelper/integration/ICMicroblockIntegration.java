package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.BlockHelperCommonProxy;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import inficraft.microblocks.core.api.multipart.ICoverSystem;
import inficraft.microblocks.core.api.multipart.IMultipartTile;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;

public class ICMicroblockIntegration extends BlockHelperInfoProvider {

    public String getMod(TileEntity te) {
        if (iof(te, "inficraft.microblocks.core.api.multipart.IMultipartTile")) {
            return "InfiMicroblocks";
        }
        return null;
    }

    @Override
    public boolean isEnabled() {
        return BlockHelperCommonProxy.icmbIntegration;
    }

    public static ItemStack getMicroblock(MovingObjectPosition mop, TileEntity te) {
        if (iof(te, "inficraft.microblocks.core.api.multipart.IMultipartTile")) {
            IMultipartTile te1 = (IMultipartTile) te;
            if (mop.subHit >= 0) {
                return te1.pickPart(mop, mop.subHit);
            } else {
                ICoverSystem ci = te1.getCoverSystem();
                if (ci != null) {
                    return ci.pickPart(mop, -1 - mop.subHit);
                }
            }
        }
        return null;
    }

}
