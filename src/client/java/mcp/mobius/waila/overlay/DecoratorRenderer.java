package mcp.mobius.waila.overlay;

import forge.IRenderWorldLastHandler;
import java.util.List;
import mcp.mobius.waila.api.IBlockDecorator;
import mcp.mobius.waila.api.impl.DataAccessorCommon;
import mcp.mobius.waila.api.impl.PluginConfig;
import mcp.mobius.waila.api.impl.WailaRegistrar;
import mcp.mobius.waila.utils.WailaExceptionHandler;
import net.minecraft.src.Block;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.RenderGlobal;
import net.minecraft.src.World;
import org.lwjgl.opengl.GL11;

public class DecoratorRenderer implements IRenderWorldLastHandler {

    @Override
    public void onRenderWorldLast(RenderGlobal renderer, float partialTicks) {
        if (RayTracing.instance().getTarget() == null || RayTracing.instance().getTargetStack() == null) return;

        DataAccessorCommon accessor = DataAccessorCommon.INSTANCE;
        World world = renderer.worldObj;
        EntityPlayer player = renderer.mc.thePlayer;
        EntityLiving viewEntity = renderer.mc.renderViewEntity;

        if (world == null || player == null || viewEntity == null) return;

        accessor.set(world, player, RayTracing.instance().getTarget(), viewEntity, partialTicks);

        Block block = accessor.getBlock();

        if (!WailaRegistrar.instance().hasBlockDecorator(block)) return;

        GL11.glPushAttrib(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);

        if (WailaRegistrar.instance().hasBlockDecorator(block)) {
            for (List<IBlockDecorator> decoratorsList :
                    WailaRegistrar.instance().getBlockDecorators(block).values()) {
                for (IBlockDecorator decorator : decoratorsList) {
                    GL11.glPushMatrix();
                    try {
                        decorator.decorateBlock(RayTracing.instance().getTargetStack(), accessor,
                                PluginConfig.instance());
                    } catch (Throwable t) {
                        WailaExceptionHandler.handleErr(t, decorator.getClass(), null);
                    }
                    GL11.glPopMatrix();
                }
            }
        }

        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopAttrib();
    }

}
