package mcp.mobius.waila.addons.redpower;

import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IDataProvider;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataAccessor;
import mcp.mobius.waila.api.ITaggedList;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumMovingObjectType;
import net.minecraft.src.ItemStack;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.TileEntity;
import net.minecraft.src.Vec3D;
import net.minecraft.src.World;

import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.BlockMultipart;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.CoverLib_convertCoverPlate;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.RedPowerWiring_blockWiring;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileCoverable;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileCoverable_getCover;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileCoverable_getCoverMask;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileLogic_getBlockID;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileExtended_getExtendedID;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileLogic;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileLogic_Rotation;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileWiring;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileWiring_CenterPost;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileWiring_ConSides;
import static mcp.mobius.waila.addons.redpower.RedPowerPlugin.TileWiring_Metadata;

public final class HUDHandlerMicroBlocks implements IDataProvider {

    public static final IDataProvider INSTANCE = new HUDHandlerMicroBlocks();

    private HUDHandlerMicroBlocks() {
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        World w = accessor.getWorld();
        EntityPlayer p = accessor.getPlayer();
        MovingObjectPosition mop = accessor.getPosition();
        Block b = accessor.getBlock();
        TileEntity tl = accessor.getTileEntity();

        try {
            if (TileCoverable.isInstance(tl) && BlockMultipart.isInstance(b)) {
                MovingObjectPosition pos = retraceBlock(w, p, mop.blockX, mop.blockY, mop.blockZ, b);
                if (pos != null && pos.typeOfHit == EnumMovingObjectType.TILE) {
                    if (TileLogic != null && TileLogic.isInstance(tl)) {
                        if (pos.subHit == TileLogic_Rotation.getInt(tl) >> 2) {
                            return new ItemStack((Integer) TileLogic_getBlockID.invoke(tl), 1,
                                    (Integer) TileExtended_getExtendedID.invoke(tl));
                        }
                        return getCover(tl, pos.subHit);
                    } else if (TileWiring != null && TileWiring.isInstance(tl)) {
                        if (pos.subHit == 26 && (TileWiring_ConSides.getInt(tl) & 64) > 0) {
                            int td = 8192 + TileWiring_CenterPost.getShort(tl);
                            int extId = (Integer) TileExtended_getExtendedID.invoke(tl);
                            if (extId == 3) td += 256;
                            Block bm = (Block) RedPowerWiring_blockWiring.get(null);
                            return new ItemStack(bm.blockID, 1, td);
                        } else {
                            if ((TileWiring_ConSides.getInt(tl) & 1 << pos.subHit) <= 0)
                                return getCover(tl, pos.subHit);
                            Block bm = (Block) RedPowerWiring_blockWiring.get(null);
                            return new ItemStack(bm.blockID, 1,
                                    (Integer) TileExtended_getExtendedID.invoke(tl) * 256 + TileWiring_Metadata.getInt(tl));
                        }
                    } else {
                        return getCover(tl, pos.subHit);
                    }
                }
            }
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, accessor.getTileEntity().getClass(), null);
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

    // Copied from BlockMultipart#harvestBlock
    // Assume: bm instanceof BlockMultipart
    private static MovingObjectPosition retraceBlock(World world, EntityPlayer player, int x, int y, int z, Block bm) {
        Vec3D org = Vec3D.createVector(player.posX, player.posY + 1.62D - (double) player.yOffset, player.posZ);
        Vec3D vec = player.getLook(1.0F);
        Vec3D end = org.addVector(vec.xCoord * 5.0D, vec.yCoord * 5.0D, vec.zCoord * 5.0D);
        return bm.collisionRayTrace(world, x, y, z, org, end);
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
