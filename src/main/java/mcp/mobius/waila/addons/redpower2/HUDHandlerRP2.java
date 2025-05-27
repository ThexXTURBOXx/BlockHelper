package mcp.mobius.waila.addons.redpower2;

import java.util.ArrayList;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.CoreLib_getTileEntity;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.CoreLib_retraceBlock;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.CoverLib_convertCoverPlate;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.RedPowerBase_blockMicro;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileCoverable;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileCoverable_getCover;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileCoverable_getCoverMask;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileExtended;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileExtended_addHarvestContents;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileExtended_getBlockID;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileExtended_getExtendedID;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileLogic;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileLogic_Cover;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileLogic_Rotation;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileLogic_SubId;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TilePipe;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileTube;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileWiring;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileWiring_CenterPost;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileWiring_ConSides;
import static mcp.mobius.waila.addons.redpower2.RedPower2Plugin.TileWiring_Metadata;

public final class HUDHandlerRP2 implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerRP2();

    private HUDHandlerRP2() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        World w = accessor.getWorld();
        EntityPlayer p = accessor.getPlayer();
        MovingObjectPosition mop = accessor.getPosition();
        TileEntity te = accessor.getTileEntity();

        try {
            if (TileCoverable.isInstance(te)) {
                MovingObjectPosition pos = (MovingObjectPosition) CoreLib_retraceBlock.invoke(null,
                        w, p, mop.blockX, mop.blockY, mop.blockZ);
                if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE) {
                    TileEntity tl = (TileEntity) CoreLib_getTileEntity.invoke(null,
                            w, mop.blockX, mop.blockY, mop.blockZ, TileCoverable);
                    if (tl != null) {
                        Block bm = (Block) RedPowerBase_blockMicro.get(null);
                        if (TileLogic != null && TileLogic.isInstance(tl)) {
                            if (pos.subHit == TileLogic_Rotation.getInt(tl) >> 2) {
                                if (TileLogic_Cover.getInt(tl) != 255) {
                                    return new ItemStack((Integer) TileExtended_getBlockID.invoke(tl), 1,
                                            (Integer) TileExtended_getExtendedID.invoke(tl) * 256 + TileLogic_SubId.getInt(tl));
                                } else {
                                    ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();
                                    TileExtended_addHarvestContents.invoke(tl, stacks);
                                    if (!stacks.isEmpty())
                                        return stacks.get(0);
                                }
                            }
                            return getCover(tl, pos.subHit);
                        } else if ((TileTube != null && TileTube.isInstance(tl)) ||
                                   (TilePipe != null && TilePipe.isInstance(tl))) {
                            if (pos.subHit == 29)
                                return new ItemStack(bm.blockID, 1,
                                        (Integer) TileExtended_getExtendedID.invoke(tl) << 8);
                            return getCover(tl, pos.subHit);
                        } else if (TileWiring != null && TileWiring.isInstance(tl)) {
                            if (pos.subHit == 29 && (TileWiring_ConSides.getInt(tl) & 64) > 0) {
                                int td = 16384 + TileWiring_CenterPost.getShort(tl);
                                int extId = (Integer) TileExtended_getExtendedID.invoke(tl);
                                if (extId == 3) td += 256;
                                if (extId == 5) td += 512;
                                return new ItemStack(bm.blockID, 1, td);
                            } else {
                                if ((TileWiring_ConSides.getInt(tl) & 1 << pos.subHit) <= 0)
                                    return getCover(tl, pos.subHit);
                                return new ItemStack(bm.blockID, 1,
                                        (Integer) TileExtended_getExtendedID.invoke(tl) * 256 + TileWiring_Metadata.getInt(tl));
                            }
                        } else {
                            return getCover(tl, pos.subHit);
                        }
                    }
                }
            } else if (TileExtended.isInstance(te)) {
                ArrayList<ItemStack> is = new ArrayList<ItemStack>();
                TileExtended_addHarvestContents.invoke(te, is);
                if (!is.isEmpty())
                    return is.get(0);
            }
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
        return null;
    }

    @Override
    public void modifyHead(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyBody(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void modifyTail(ItemStack itemStack, ITaggedList<String, String> currenttip,
                           IDataAccessor accessor, IPluginConfig config) {
    }

    @Override
    public void appendServerData(TileEntity te, NBTTagCompound tag,
                                 IServerDataAccessor accessor, IPluginConfig config) {
    }

    // Copied from TileCoverable#onHarvestPart
    // Assume: te instanceof TileCoverable
    private static ItemStack getCover(TileEntity te, int subHit) throws Throwable {
        if ((((Integer) TileCoverable_getCoverMask.invoke(te)) & 1 << subHit) != 0) {
            int tr = (Integer) TileCoverable_getCover.invoke(te, subHit);
            if (tr >= 0)
                return (ItemStack) CoverLib_convertCoverPlate.invoke(null, subHit, tr);
        }
        return null;
    }

}
