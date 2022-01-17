package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import eloraam.core.BlockMultipart;
import eloraam.core.CoverLib;
import eloraam.core.TileCoverable;
import eloraam.intred.TileLogic;
import eloraam.wiring.TileWiring;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

public final class MicroblockIntegration extends BlockHelperInfoProvider {

    private MicroblockIntegration() {
        throw new UnsupportedOperationException();
    }

    public static ItemStack getMicroblock(World w, EntityPlayer p, MovingObjectPosition mop, TileEntity tl, Block b) {
        if (iof(tl, "eloraam.core.TileCoverable") && iof(b, "eloraam.core.BlockMultipart")) {
            MovingObjectPosition pos = retraceBlock(w, p, mop.blockX, mop.blockY, mop.blockZ, (BlockMultipart) b);
            if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE) {
                if (tl instanceof TileLogic) {
                    TileLogic tlo = (TileLogic) tl;
                    if (pos.subHit == tlo.Rotation >> 2) {
                        return new ItemStack(tlo.getBlockID(), 1, tlo.getExtendedID());
                    }
                    return getCover(tlo, pos.subHit);
                } else if (tl instanceof TileWiring) {
                    TileWiring tw = (TileWiring) tl;
                    if (pos.subHit == 26 && (tw.ConSides & 64) > 0) {
                        int td = 8192 + tw.CenterPost;
                        if (tw.getExtendedID() == 3) {
                            td += 256;
                        }
                        Block bm = getField(getClass("RedPowerWiring"), null, "blockWiring");
                        return new ItemStack(bm.blockID, 1, td);
                    } else {
                        if ((tw.ConSides & 1 << pos.subHit) <= 0) {
                            return getCover(tl, pos.subHit);
                        }
                        Block bm = getField(getClass("RedPowerWiring"), null, "blockWiring");
                        return new ItemStack(bm.blockID, 1, tw.getExtendedID() * 256 + tw.Metadata);
                    }
                } else {
                    return getCover(tl, pos.subHit);
                }
            }
        }
        return null;
    }

    // Copied from BlockMultipart#harvestBlock
    private static MovingObjectPosition retraceBlock(World world, EntityPlayer player, int x, int y, int z,
                                                     BlockMultipart bm) {
        Vec3D org = Vec3D.createVector(player.posX, player.posY + 1.62D - (double) player.yOffset, player.posZ);
        Vec3D vec = player.getLook(1.0F);
        Vec3D end = org.addVector(vec.xCoord * 5.0D, vec.yCoord * 5.0D, vec.zCoord * 5.0D);
        return bm.a(world, x, y, z, org, end);
    }

    // Copied from TileCoverable#onHarvestPart
    private static ItemStack getCover(TileEntity te, int subHit) {
        TileCoverable tc = (TileCoverable) te;
        if ((tc.getCoverMask() & 1 << subHit) != 0) {
            int tr = tc.getCover(subHit);
            if (tr >= 0) {
                return CoverLib.convertCoverPlate(subHit, tr);
            }
        }
        return null;
    }

}
