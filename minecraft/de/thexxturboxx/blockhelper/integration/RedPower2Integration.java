package de.thexxturboxx.blockhelper.integration;

import de.thexxturboxx.blockhelper.api.BlockHelperBlockState;
import de.thexxturboxx.blockhelper.api.BlockHelperInfoProvider;
import de.thexxturboxx.blockhelper.api.InfoHolder;
import eloraam.core.CoreLib;
import eloraam.core.CoverLib;
import eloraam.core.TileCoverable;
import eloraam.machine.TilePipe;
import eloraam.machine.TileTube;
import eloraam.wiring.TileWiring;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class RedPower2Integration extends BlockHelperInfoProvider {

    @Override
    public void addInformation(BlockHelperBlockState state, InfoHolder info) {
        if (iof(state.block, "eloraam.world.BlockCustomCrops")) {
            if (state.meta < 5) {
                int grow = (int) ((state.meta / 4d) * 100);
                String toShow;
                if (grow >= 100) {
                    toShow = "Mature";
                } else {
                    toShow = grow + "%";
                }
                info.add("Growth State: " + toShow);
            } else {
                info.add("Growth State: Ripe");
            }
        }
    }

    @Override
    public ItemStack getItemStack(BlockHelperBlockState state) {
        if (iof(state.block, "eloraam.world.BlockCustomCrops")) {
            return new ItemStack(BlockHelperInfoProvider.<Item>getStaticField(getClass("RedPowerWorld"), "itemSeeds"));
        }
        return super.getItemStack(state);
    }

    public static ItemStack getMicroblock(World w, EntityPlayer p, MovingObjectPosition mop, TileEntity te) {
        if (iof(te, "eloraam.core.TileCoverable")) {
            MovingObjectPosition pos = CoreLib.retraceBlock(w, p, mop.blockX,
                    mop.blockY, mop.blockZ);
            if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE) {
                TileCoverable tl = (TileCoverable) CoreLib.getTileEntity(w, mop.blockX,
                        mop.blockY, mop.blockZ, TileCoverable.class);
                if (tl != null) {
                    if (tl instanceof TileTube || tl instanceof TilePipe) {
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

    private static ItemStack getCover(TileCoverable tc, int subHit) {
        if ((tc.getCoverMask() & 1 << subHit) != 0) {
            int tr = tc.getCover(subHit);
            if (tr >= 0) {
                return CoverLib.convertCoverPlate(subHit, tr);
            }
        }
        return null;
    }

}
