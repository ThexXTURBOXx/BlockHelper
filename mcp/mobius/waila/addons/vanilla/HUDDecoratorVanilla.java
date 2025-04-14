package mcp.mobius.waila.addons.vanilla;

import mcp.mobius.waila.api.IBlockDecorator;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.gui.helpers.UIHelper;
import net.minecraft.block.BlockDirectional;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;

public class HUDDecoratorVanilla implements IBlockDecorator {

    @Override
    public void decorateBlock(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        if (config.get("vanilla.repeaterol")) {
            Tessellator tessellator = Tessellator.instance;

            //UIHelper.drawBillboardText(itemStack.getDisplayName(), accessor.getRenderingPosition(), 0.5F, 1.5F, 0.5F,
            // accessor.getPartialFrame());
            int dir = BlockDirectional.getDirection(accessor.getMetadata());
            UIHelper.drawFloatingText(dir == 0 ? "OUT" : "IN", accessor.getRenderingPosition(),
                    0.5F, 0.2F, -0.2F, 90F, 0F, 0F);
            UIHelper.drawFloatingText(dir == 3 ? "OUT" : "IN", accessor.getRenderingPosition(),
                    -0.2F, 0.2F, 0.5F, 90F, 90F, 0F);
            UIHelper.drawFloatingText(dir == 1 ? "OUT" : "IN", accessor.getRenderingPosition(),
                    1.2F, 0.2F, 0.5F, 90F, -90F, 0F);
            UIHelper.drawFloatingText(dir == 2 ? "OUT" : "IN", accessor.getRenderingPosition(),
                    0.5F, 0.2F, 1.2F, 90F, -180F, 0F);

            final double yOff = 0.2;
            final double xzOff = 0.1;
            final double xzDelta = 1 + 2 * xzOff;

            double x = accessor.getRenderingPosition().xCoord - xzOff;
            double y = accessor.getRenderingPosition().yCoord - xzOff;
            double z = accessor.getRenderingPosition().zCoord - xzOff;

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
