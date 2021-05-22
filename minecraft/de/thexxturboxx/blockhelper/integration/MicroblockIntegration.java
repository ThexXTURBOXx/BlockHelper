package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import eloraam.core.CoreLib;
import eloraam.core.CoverLib;
import eloraam.core.TileCoverable;
import eloraam.logic.TileLogic;
import eloraam.machine.TilePipe;
import eloraam.machine.TileTube;
import eloraam.wiring.TileWiring;
import java.util.ArrayList;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public final class MicroblockIntegration extends BlockHelperInfoProvider {

    private MicroblockIntegration() {
        throw new UnsupportedOperationException();
    }

    public static ItemStack getMicroblock(World w, EntityPlayer p, MovingObjectPosition mop, TileEntity te) {
        if (iof(te, "eloraam.core.TileCoverable")) {
            MovingObjectPosition pos = CoreLib.retraceBlock(w, p, mop.blockX, mop.blockY, mop.blockZ);
            if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE) {
                TileCoverable tl = (TileCoverable) CoreLib.getTileEntity(w, mop.blockX, mop.blockY, mop.blockZ,
                        TileCoverable.class);
                if (tl != null) {
                    if (tl instanceof TileLogic) {
                        TileLogic tlo = (TileLogic) tl;
                        if (pos.subHit == tlo.Rotation >> 2) {
                            if (tlo.Cover != 255) {
                                return new ItemStack(tlo.getBlockID(), 1, tlo.getExtendedID() * 256 + tlo.SubId);
                            } else {
                                ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
                                tlo.addHarvestContents(stacks);
                                if (!stacks.isEmpty()) {
                                    return stacks.get(0);
                                }
                            }
                        }
                        return getCover(tlo, pos.subHit);
                    } else if (tl instanceof TileTube || tl instanceof TilePipe) {
                        if (pos.subHit == 29) {
                            Block bm = BlockHelperInfoProvider.getStaticField(
                                    getClass("RedPowerBase"), "blockMicro");
                            return new ItemStack(bm.blockID, 1, tl.getExtendedID() << 8);
                        }
                        return getCover(tl, pos.subHit);
                    } else if (tl instanceof TileWiring) {
                        TileWiring tw = (TileWiring) tl;
                        if (pos.subHit == 29 && (tw.ConSides & 64) > 0) {
                            int td = 16384 + tw.CenterPost;
                            if (tw.getExtendedID() == 3) {
                                td += 256;
                            }
                            if (tw.getExtendedID() == 5) {
                                td += 512;
                            }
                            Block bm = BlockHelperInfoProvider.getStaticField(
                                    getClass("RedPowerBase"), "blockMicro");
                            return new ItemStack(bm.blockID, 1, td);
                        } else {
                            if ((tw.ConSides & 1 << pos.subHit) <= 0) {
                                return getCover(tl, pos.subHit);
                            }
                            Block bm = BlockHelperInfoProvider.getStaticField(
                                    getClass("RedPowerBase"), "blockMicro");
                            return new ItemStack(bm.blockID, 1, tw.getExtendedID() * 256 + tw.Metadata);
                        }
                    } else {
                        return getCover(tl, pos.subHit);
                    }
                }
            }
        }
        return null;
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
