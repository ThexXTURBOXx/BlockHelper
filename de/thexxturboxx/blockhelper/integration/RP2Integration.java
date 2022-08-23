package de.thexxturboxx.blockhelper.integration;

import com.eloraam.redpower.RedPowerBase;
import com.eloraam.redpower.RedPowerWorld;
import com.eloraam.redpower.core.CoreLib;
import com.eloraam.redpower.core.CoverLib;
import com.eloraam.redpower.core.TileCoverable;
import com.eloraam.redpower.core.TileExtended;
import com.eloraam.redpower.logic.TileLogic;
import com.eloraam.redpower.machine.TilePipe;
import com.eloraam.redpower.machine.TileTube;
import com.eloraam.redpower.wiring.TileWiring;
import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import java.util.ArrayList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class RP2Integration extends BlockHelperInfoProvider {

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.block, "com.eloraam.redpower.world.BlockCustomCrops")) {
            return new ItemStack(
                    BlockHelperInfoProvider.<Integer>getField(Item.class, RedPowerWorld.itemSeeds, "cj"), 1, 0);
        }
        return super.getItemStack(state);
    }

    public static ItemStack getMicroblock(World w, EntityPlayer p, MovingObjectPosition mop, TileEntity te) {
        if (iof(te, "com.eloraam.redpower.core.TileCoverable")) {
            MovingObjectPosition pos = CoreLib.retraceBlock(w, p, mop.blockX, mop.blockY, mop.blockZ);
            if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE) {
                TileCoverable tl = (TileCoverable) CoreLib.getTileEntity(w, mop.blockX, mop.blockY, mop.blockZ,
                        TileCoverable.class);
                if (tl != null) {
                    Block bm = RedPowerBase.blockMicro;
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
                            return new ItemStack(bm.blockID, 1, td);
                        } else {
                            if ((tw.ConSides & 1 << pos.subHit) <= 0) {
                                return getCover(tl, pos.subHit);
                            }
                            return new ItemStack(bm.blockID, 1,
                                    tw.getExtendedID() * 256 + tw.Metadata);
                        }
                    } else {
                        return getCover(tl, pos.subHit);
                    }
                }
            }
        } else if (iof(te, "com.eloraam.redpower.core.TileExtended")) {
            ArrayList<ItemStack> is = new ArrayList<ItemStack>();
            ((TileExtended) te).addHarvestContents(is);
            if (!is.isEmpty()) {
                return is.get(0);
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
