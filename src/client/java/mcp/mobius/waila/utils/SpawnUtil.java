package mcp.mobius.waila.utils;

import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Chunk;
import net.minecraft.src.Entity;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.World;

public final class SpawnUtil {

    private SpawnUtil() {
        throw new UnsupportedOperationException();
    }

    public static byte getSpawnMode(World w, int x, int y, int z) {
        return getSpawnMode(w.getChunkFromBlockCoords(x, z), AxisAlignedBB.getBoundingBoxFromPool(
                0, 0, 0, 0, 0, 0), x, y, z);
    }

    public static byte getSpawnMode(Chunk chunk, AxisAlignedBB aabb, int x, int y, int z) {
        try {
            if (!canCreatureTypeSpawnAtLocation(chunk.worldObj, x, y, z) ||
                chunk.getSavedLightValue(EnumSkyBlock.Block, x & 0xF, y, z & 0xF) >= 8)
                return 0;
            aabb.minX = x + 0.2;
            aabb.maxX = x + 0.8;
            aabb.minY = y + 0.01;
            aabb.maxY = y + 1.8;
            aabb.minZ = z + 0.2;
            aabb.maxZ = z + 0.8;
            if (!chunk.worldObj.checkIfAABBIsClear(aabb) ||
                !chunk.worldObj.getEntitiesWithinAABB(Entity.class, aabb).isEmpty() || chunk.worldObj.getIsAnyLiquid(aabb))
                return 0;
            if (chunk.getSavedLightValue(EnumSkyBlock.Sky, x & 0xF, y, z & 0xF) >= 8)
                return 1;
            return 2;
        } catch (Throwable t) {
            WailaExceptionHandler.handleErr(t, "SpawnUtil#getSpawnMode", null);
            return 1;
        }
    }

    public static boolean canCreatureTypeSpawnAtLocation(World world, int x, int y, int z) {
        return world.isBlockOpaqueCube(x, y - 1, z) &&
               !world.isBlockOpaqueCube(x, y, z) &&
               !world.getBlockMaterial(x, y, z).getIsLiquid() &&
               !world.isBlockOpaqueCube(x, y + 1, z);
    }

}
