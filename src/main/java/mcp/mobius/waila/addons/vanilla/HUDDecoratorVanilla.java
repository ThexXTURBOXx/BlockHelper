package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IBlockDecorator;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.gui.helpers.UIHelper;
import net.minecraft.src.BlockDirectional;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Tessellator;
import net.minecraft.src.Vec3;

public final class HUDDecoratorVanilla implements IBlockDecorator {

    public static final IBlockDecorator INSTANCE = new HUDDecoratorVanilla();

    private HUDDecoratorVanilla() {
    }

    @Override
    public void decorateBlock(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        if (config.get("vanilla.repeaterol")) {
            Tessellator tessellator = Tessellator.instance;

            int meta = accessor.getMetadata();
            int dir = BlockDirectional.getDirection(meta);
            Vec3 renderPos = accessor.getRenderingPosition();

            UIHelper.drawFloatingText(dir % 2 == 1 ? "LOCK" : dir == 0 ? "OUT" : "IN", renderPos,
                    0.5F, 0.2F, -0.2F, 90F, 0F, 0F);
            UIHelper.drawFloatingText(dir % 2 == 0 ? "LOCK" : dir == 3 ? "OUT" : "IN", renderPos,
                    -0.2F, 0.2F, 0.5F, 90F, 90F, 0F);
            UIHelper.drawFloatingText(dir % 2 == 0 ? "LOCK" : dir == 1 ? "OUT" : "IN", renderPos,
                    1.2F, 0.2F, 0.5F, 90F, -90F, 0F);
            UIHelper.drawFloatingText(dir % 2 == 1 ? "LOCK" : dir == 2 ? "OUT" : "IN", renderPos,
                    0.5F, 0.2F, 1.2F, 90F, -180F, 0F);

            final double yOff = 0.2;
            final double xzOff = 0.1;
            final double xzDelta = 1 + 2 * xzOff;

            double x = renderPos.xCoord - xzOff;
            double y = renderPos.yCoord - xzOff;
            double z = renderPos.zCoord - xzOff;

            tessellator.startDrawingQuads();

            tessellator.setColorRGBA(255, 255, 255, 150);

            tessellator.addVertex(x, y + yOff, z);
            tessellator.addVertex(x, y + yOff, z + xzDelta / 2 - 0.1);
            tessellator.addVertex(x + xzOff, y + yOff, z + xzDelta / 2 - 0.1);
            tessellator.addVertex(x + xzOff, y + yOff, z);

            tessellator.addVertex(x, y + yOff, z + xzDelta / 2 + 0.1);
            tessellator.addVertex(x, y + yOff, z + xzDelta);
            tessellator.addVertex(x + xzOff, y + yOff, z + xzDelta);
            tessellator.addVertex(x + xzOff, y + yOff, z + xzDelta / 2 + 0.1);

            tessellator.addVertex(x + xzDelta - 0.1, y + yOff, z + 0.1);
            tessellator.addVertex(x + xzDelta - 0.1, y + yOff, z + xzDelta / 2 - 0.1);
            tessellator.addVertex(x + xzDelta + xzOff - 0.1, y + yOff, z + xzDelta / 2 - 0.1);
            tessellator.addVertex(x + xzDelta + xzOff - 0.1, y + yOff, z + 0.1);

            tessellator.addVertex(x + xzDelta - 0.1, y + yOff, z + xzDelta / 2 + 0.1);
            tessellator.addVertex(x + xzDelta - 0.1, y + yOff, z + xzDelta);
            tessellator.addVertex(x + xzDelta + xzOff - 0.1, y + yOff, z + xzDelta);
            tessellator.addVertex(x + xzDelta + xzOff - 0.1, y + yOff, z + xzDelta / 2 + 0.1);


            tessellator.addVertex(x + 0.1, y + yOff, z);
            tessellator.addVertex(x + 0.1, y + yOff, z + xzOff);
            tessellator.addVertex(x + xzDelta / 2 - 0.1, y + yOff, z + xzOff);
            tessellator.addVertex(x + xzDelta / 2 - 0.1, y + yOff, z);

            tessellator.addVertex(x + xzDelta / 2 + 0.1, y + yOff, z);
            tessellator.addVertex(x + xzDelta / 2 + 0.1, y + yOff, z + xzOff);
            tessellator.addVertex(x + xzDelta, y + yOff, z + xzOff);
            tessellator.addVertex(x + xzDelta, y + yOff, z);

            tessellator.addVertex(x + 0.1, y + yOff, z + xzDelta - 0.1);
            tessellator.addVertex(x + 0.1, y + yOff, z + xzOff + xzDelta - 0.1);
            tessellator.addVertex(x + xzDelta / 2 - 0.1, y + yOff, z + xzOff + xzDelta - 0.1);
            tessellator.addVertex(x + xzDelta / 2 - 0.1, y + yOff, z + xzDelta - 0.1);

            tessellator.addVertex(x + xzDelta / 2 + 0.1, y + yOff, z + xzDelta - 0.1);
            tessellator.addVertex(x + xzDelta / 2 + 0.1, y + yOff, z + xzOff + xzDelta - 0.1);
            tessellator.addVertex(x + xzDelta - 0.1, y + yOff, z + xzOff + xzDelta - 0.1);
            tessellator.addVertex(x + xzDelta - 0.1, y + yOff, z + xzDelta - 0.1);

            tessellator.draw();
        }
    }

}
