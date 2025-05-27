package mcp.mobius.waila.utils;

import java.lang.reflect.Method;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.Chunk;
import net.minecraft.src.Entity;
import net.minecraft.src.EnumCreatureType;
import net.minecraft.src.EnumSkyBlock;
import net.minecraft.src.SpawnerAnimals;
import net.minecraft.src.World;

public final class SpawnUtil {

    private static final Method canCreatureTypeSpawnAtLocation;

    static {
        try {
            canCreatureTypeSpawnAtLocation = AccessHelper.getDeclaredMethod(SpawnerAnimals.class,
                    new Class[]{EnumCreatureType.class, World.class, int.class, int.class, int.class},
                    "a", "func_21203_a", "method_1871", "canCreatureTypeSpawnAtLocation");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    private SpawnUtil() {
        throw new UnsupportedOperationException();
    }

    public static byte getSpawnMode(World w, int x, int y, int z) {
        return getSpawnMode(w.getChunkFromBlockCoords(x, z), AxisAlignedBB.getBoundingBoxFromPool(
                0, 0, 0, 0, 0, 0), x, y, z);
    }

    public static byte getSpawnMode(Chunk chunk, AxisAlignedBB aabb, int x, int y, int z) {
        try {
            boolean flag = (Boolean) canCreatureTypeSpawnAtLocation.invoke(null,
                    EnumCreatureType.monster, chunk.worldObj, x, y, z);
            if (!flag || chunk.getSavedLightValue(EnumSkyBlock.Block, x & 0xF, y, z & 0xF) >= 8)
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

}
