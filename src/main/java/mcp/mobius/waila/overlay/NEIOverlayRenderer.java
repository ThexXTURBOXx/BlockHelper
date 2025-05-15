package mcp.mobius.waila.overlay;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.utils.SpawnUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.ForgeSubscribe;
import org.lwjgl.opengl.GL11;

public class NEIOverlayRenderer {

    public static int renderChunkBounds = 0;
    public static boolean renderMobSpawnOverlay = false;

    @ForgeSubscribe
    @SideOnly(Side.CLIENT)
    public void onRenderWorldLast(RenderWorldLastEvent event) {
        GL11.glPushMatrix();

        Entity entity = event.context.mc.renderViewEntity;
        double interpPosX = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * event.partialTicks;
        double interpPosY = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * event.partialTicks;
        double interpPosZ = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * event.partialTicks;
        GL11.glTranslated(-interpPosX, -interpPosY, -interpPosZ);

        renderChunkBounds(entity);
        renderMobSpawnOverlay(entity);

        GL11.glPopMatrix();
    }

    public static double clamp(double value, double min, double max) {
        return value < min ? min : Math.min(value, max);
    }

    private static void renderMobSpawnOverlay(Entity entity) {
        if (!renderMobSpawnOverlay) return;

        boolean oldOverlay = PluginConfig.instance().get("general.oldlightlevelol");

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(GL11.GL_LINES);
        GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
        int curSpawnMode = 2;
        World world = entity.worldObj;
        int x1 = (int) entity.posX;
        int z1 = (int) entity.posZ;
        int y1 = (int) clamp(entity.posY, 16.0, world.getHeight() - 16);
        AxisAlignedBB aabb = AxisAlignedBB.getBoundingBox(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        for (int x2 = x1 - 16; x2 <= x1 + 16; ++x2) {
            for (int z2 = z1 - 16; z2 <= z1 + 16; ++z2) {
                Chunk chunk = world.getChunkFromBlockCoords(x2, z2);
                BiomeGenBase biome = world.getBiomeGenForCoords(x2, z2);
                if (!biome.getSpawnableList(EnumCreatureType.monster).isEmpty() &&
                    biome.getSpawningChance() > 0.0f) {
                    for (int y2 = y1 - 16; y2 < y1 + 16; ++y2) {
                        int spawnMode = SpawnUtil.getSpawnMode(chunk, aabb, x2, y2, z2);
                        if (spawnMode != 0) {
                            if (spawnMode != curSpawnMode) {
                                if (spawnMode == 2 || oldOverlay)
                                    GL11.glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
                                else
                                    GL11.glColor4f(1.0f, 1.0f, 0.0f, 1.0f);
                                curSpawnMode = spawnMode;
                            }
                            GL11.glVertex3d(x2, y2 + 0.004, z2);
                            GL11.glVertex3d(x2 + 1, y2 + 0.004, z2 + 1);
                            GL11.glVertex3d(x2 + 1, y2 + 0.004, z2);
                            GL11.glVertex3d(x2, y2 + 0.004, z2 + 1);
                        }
                    }
                }
            }
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private static void renderChunkBounds(Entity entity) {
        if (renderChunkBounds == 0) return;

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glLineWidth(1.5f);
        GL11.glBegin(GL11.GL_LINES);
        for (int cx = -4; cx <= 4; ++cx) {
            for (int cz = -4; cz <= 4; ++cz) {
                double x1 = entity.chunkCoordX + cx << 4;
                double z1 = entity.chunkCoordZ + cz << 4;
                double x2 = x1 + 16.0;
                double z2 = z1 + 16.0;
                double dy = 128.0;
                double y1 = Math.floor(entity.posY - dy / 2.0);
                double y2 = y1 + dy;
                if (y1 < 0.0) {
                    y1 = 0.0;
                    y2 = dy;
                }
                if (y1 > entity.worldObj.getHeight()) {
                    y2 = entity.worldObj.getHeight();
                    y1 = y2 - dy;
                }
                double dist = Math.pow(1.5, -(cx * cx + cz * cz));
                GL11.glColor4d(0.9, 0.0, 0.0, dist);
                if (cx >= 0 && cz >= 0) {
                    GL11.glVertex3d(x2, y1, z2);
                    GL11.glVertex3d(x2, y2, z2);
                }
                if (cx >= 0 && cz <= 0) {
                    GL11.glVertex3d(x2, y1, z1);
                    GL11.glVertex3d(x2, y2, z1);
                }
                if (cx <= 0 && cz >= 0) {
                    GL11.glVertex3d(x1, y1, z2);
                    GL11.glVertex3d(x1, y2, z2);
                }
                if (cx <= 0 && cz <= 0) {
                    GL11.glVertex3d(x1, y1, z1);
                    GL11.glVertex3d(x1, y2, z1);
                }
                if (renderChunkBounds == 2 && cx == 0 && cz == 0) {
                    dy = 32.0;
                    y1 = Math.floor(entity.posY - dy / 2.0);
                    y2 = y1 + dy;
                    if (y1 < 0.0) {
                        y1 = 0.0;
                        y2 = dy;
                    }
                    if (y1 > entity.worldObj.getHeight()) {
                        y2 = entity.worldObj.getHeight();
                        y1 = y2 - dy;
                    }
                    GL11.glColor4d(0.0, 0.9, 0.0, 0.4);
                    for (double y3 = (int) y1; y3 <= y2; ++y3) {
                        GL11.glVertex3d(x2, y3, z1);
                        GL11.glVertex3d(x2, y3, z2);
                        GL11.glVertex3d(x1, y3, z1);
                        GL11.glVertex3d(x1, y3, z2);
                        GL11.glVertex3d(x1, y3, z2);
                        GL11.glVertex3d(x2, y3, z2);
                        GL11.glVertex3d(x1, y3, z1);
                        GL11.glVertex3d(x2, y3, z1);
                    }
                    for (double h = 1.0; h <= 15.0; ++h) {
                        GL11.glVertex3d(x1 + h, y1, z1);
                        GL11.glVertex3d(x1 + h, y2, z1);
                        GL11.glVertex3d(x1 + h, y1, z2);
                        GL11.glVertex3d(x1 + h, y2, z2);
                        GL11.glVertex3d(x1, y1, z1 + h);
                        GL11.glVertex3d(x1, y2, z1 + h);
                        GL11.glVertex3d(x2, y1, z1 + h);
                        GL11.glVertex3d(x2, y2, z1 + h);
                    }
                }
            }
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

}
