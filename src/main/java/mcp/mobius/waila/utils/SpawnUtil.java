package mcp.mobius.waila.utils;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.SpawnerAnimals;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public final class SpawnUtil {

    private SpawnUtil() {
        throw new UnsupportedOperationException();
    }

    public static byte getSpawnMode(World w, int x, int y, int z) {
        BiomeGenBase biome = w.getBiomeGenForCoords(x, z);
        if (!biome.getSpawnableList(EnumCreatureType.monster).isEmpty() && biome.getSpawningChance() > 0.0f)
            return getSpawnMode(w.getChunkFromBlockCoords(x, z), AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(
                    0, 0, 0, 0, 0, 0), x, y, z);
        return 0;
    }

    public static byte getSpawnMode(Chunk chunk, AxisAlignedBB aabb, int x, int y, int z) {
        if (!SpawnerAnimals.canCreatureTypeSpawnAtLocation(EnumCreatureType.monster, chunk.worldObj, x, y, z) ||
            chunk.getSavedLightValue(EnumSkyBlock.Block, x & 0xF, y, z & 0xF) >= 8)
            return 0;
        aabb.minX = x + 0.2;
        aabb.maxX = x + 0.8;
        aabb.minY = y + 0.01;
        aabb.maxY = y + 1.8;
        aabb.minZ = z + 0.2;
        aabb.maxZ = z + 0.8;
        if (!chunk.worldObj.checkIfAABBIsClear(aabb) ||
            !chunk.worldObj.getAllCollidingBoundingBoxes(aabb).isEmpty() || chunk.worldObj.isAnyLiquid(aabb))
            return 0;
        if (chunk.getSavedLightValue(EnumSkyBlock.Sky, x & 0xF, y, z & 0xF) >= 8)
            return 1;
        return 2;
    }

}
