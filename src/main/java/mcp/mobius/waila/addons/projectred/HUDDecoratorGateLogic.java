package mcp.mobius.waila.addons.projectred;

import mcp.mobius.waila.api.IBlockDecorator;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.gui.helpers.UIHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.ForgeDirection;

public final class HUDDecoratorGateLogic implements IBlockDecorator {

    public static final IBlockDecorator INSTANCE = new HUDDecoratorGateLogic();

    static final byte[][] IOARRAY = {
            {1, 1, 2, 1} /* AND          */,
            {1, 1, 2, 1} /* OR           */,
            {1, 2, 2, 2} /* NOT          */,
            {2, 1, 2, 1} /* RSLatch      */,
            {2, 1, 2, 1} /* Toggle       */,
            {1, 1, 2, 1} /* NOR          */,
            {1, 1, 2, 1} /* NAND         */,
            {0, 1, 2, 1} /* XOR          */,
            {0, 1, 2, 1} /* XNOR         */,
            {1, 2, 2, 2} /* Buffer       */,
            {3, 4, 2, 5} /* Multiplexer  */,
            {1, 0, 2, 0} /* Repeater     */,
            {6, 2, 2, 2} /* Timer        */,
            {7, 2, 8, 2} /* Counter      */,
            {2, 2, 2, 2} /* Sequencer    */,
            {1, 0, 2, 0} /* PulseFormer  */,
            {1, 2, 2, 2} /* Randomizer   */,
            {6, 1, 2, 2} /* StateCell    */,
            {12, 1, 2, 1} /* Synchronizer */,
            {1, 2, 2, 10} /* DLatch       */,
            {1, 2, 2, 11} /* DFlop        */,
            {9, 0, 9, 10} /* BundledLatch */,
            {9, 0, 9, 1} /* BundledRelay */,
            {3, 4, 9, 5} /* BundledMux   */,
            {2, 0, 0, 0} /* LightSensor  */,
            {2, 0, 0, 0} /* RainSensor   */,
    };

    static final String[] IONAMES = {"", "IN", "OUT", "SWAP", "IN_A", "IN_B", "LOCK",
            "POS", "NEG", "BUS", "EN", "CLK", "RST"};

    static final int[][] LABEL_ROT_MAT = new int[][]{{0, 0, 0, 2, 3, 1}, {0, 0, 0, 2, 1, 3},
            {0, 2, 0, 0, 1, 3}, {0, 2, 0, 0, 3, 1}, {1, 3, 0, 2, 0, 0}, {3, 1, 0, 2, 0, 0}};

    private HUDDecoratorGateLogic() {
    }

    @Override
    public void decorateBlock(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get("pr.showio")) return;

        int side = accessor.getNBTInteger("side"); // Side on block
        int front = accessor.getNBTInteger("front"); // Orientation
        int type = accessor.getNBTInteger("type");
        if (side < 0 || front < 0 || side >= 6 || front >= 6 ||
            type < 0 || type >= IOARRAY.length) return;

        Vec3 renderPos = accessor.getRenderingPosition();
        double x = renderPos.xCoord;
        double y = renderPos.yCoord;
        double z = renderPos.zCoord;

        ForgeDirection orient = ForgeDirection.getOrientation(side);

        String[] IOStr = new String[4];
        int rot = LABEL_ROT_MAT[side][front];
        for (int i = 0; i < 4; i++) {
            IOStr[i] = IONAMES[IOARRAY[type][(i - rot + 16) % 4]];
        }

        switch (orient) {
        case DOWN:
            UIHelper.drawFloatingText(IOStr[0], renderPos, 0.5F, 0.2F, 1.4F, 90F, 0F, 0F);
            UIHelper.drawFloatingText(IOStr[1], renderPos, -0.4F, 0.2F, 0.5F, 90F, 270F, 0F);
            UIHelper.drawFloatingText(IOStr[2], renderPos, 0.5F, 0.2F, -0.4F, 90F, 180F, 0F);
            UIHelper.drawFloatingText(IOStr[3], renderPos, 1.4F, 0.2F, 0.5F, 90F, 90F, 0F);

            UIHelper.drawRectangle(x - 0.1, y + 0.1, z + 0.1, x, y + 0.1, z + 0.35, 255, 255, 255, 150);
            UIHelper.drawRectangle(x - 0.1, y + 0.1, z + 0.65, x, y + 0.1, z + 1.0, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.0, y + 0.1, z + 0.1, x + 1.1, y + 0.1, z + 0.35, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.0, y + 0.1, z + 0.65, x + 1.1, y + 0.1, z + 1.0, 255, 255, 255, 150);

            UIHelper.drawRectangle(x - 0.1, y + 0.1, z - 0.1, x + 0.35, y + 0.1, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 0.65, y + 0.1, z - 0.1, x + 1.1, y + 0.1, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x - 0.1, y + 0.1, z + 1.0, x + 0.35, y + 0.1, z + 1.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 0.65, y + 0.1, z + 1.0, x + 1.1, y + 0.1, z + 1.1, 255, 255, 255, 150);
            break;

        case UP:
            UIHelper.drawFloatingText(IOStr[0], renderPos, 0.5F, 0.8F, 1.4F, 270F, 180F, 0F);
            UIHelper.drawFloatingText(IOStr[1], renderPos, 1.4F, 0.8F, 0.5F, 270F, 270F, 0F);
            UIHelper.drawFloatingText(IOStr[2], renderPos, 0.5F, 0.8F, -0.4F, 270F, 0F, 0F);
            UIHelper.drawFloatingText(IOStr[3], renderPos, -0.4F, 0.8F, 0.5F, 270F, 90F, 0F);

            UIHelper.drawRectangle(x, y + 0.9, z + 0.1, x - 0.1, y + 0.9, z + 0.35, 255, 255, 255, 150);
            UIHelper.drawRectangle(x, y + 0.9, z + 0.65, x - 0.1, y + 0.9, z + 1.0, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.1, y + 0.9, z + 0.1, x + 1.0, y + 0.9, z + 0.35, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.1, y + 0.9, z + 0.65, x + 1.0, y + 0.9, z + 1.0, 255, 255, 255, 150);

            UIHelper.drawRectangle(x + 0.35, y + 0.9, z - 0.1, x - 0.1, y + 0.9, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.1, y + 0.9, z - 0.1, x + 0.65, y + 0.9, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 0.35, y + 0.9, z + 1.0, x - 0.1, y + 0.9, z + 1.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.1, y + 0.9, z + 1.0, x + 0.65, y + 0.9, z + 1.1, 255, 255, 255, 150);
            break;

        case NORTH:
            UIHelper.drawFloatingText(IOStr[0], renderPos, 0.5F, 1.4F, 0.2F, 0F, 180F, 0F);
            UIHelper.drawFloatingText(IOStr[1], renderPos, 1.4F, 0.5F, 0.2F, 0F, 180F, 90F);
            UIHelper.drawFloatingText(IOStr[2], renderPos, 0.5F, -0.4F, 0.2F, 0F, 180F, 180F);
            UIHelper.drawFloatingText(IOStr[3], renderPos, -0.4F, 0.5F, 0.2F, 0F, 180F, 270F);

            UIHelper.drawRectangle(x - 0.1, y, z + 0.1, x, y + 0.35, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x - 0.1, y + 0.65, z + 0.1, x, y + 1.0, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.0, y, z + 0.1, x + 1.1, y + 0.35, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.0, y + 0.65, z + 0.1, x + 1.1, y + 1.0, z + 0.1, 255, 255, 255, 150);

            UIHelper.drawRectangle(x - 0.1, y - 0.1, z + 0.1, x + 0.35, y, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 0.65, y - 0.1, z + 0.1, x + 1.1, y, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x - 0.1, y + 1.0, z + 0.1, x + 0.35, y + 1.1, z + 0.1, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 0.65, y + 1.0, z + 0.1, x + 1.1, y + 1.1, z + 0.1, 255, 255, 255, 150);
            break;

        case SOUTH:
            UIHelper.drawFloatingText(IOStr[0], renderPos, 0.5F, 1.4F, 0.8F, 0F, 0F, 0F);
            UIHelper.drawFloatingText(IOStr[1], renderPos, -0.4F, 0.5F, 0.8F, 0F, 0F, 90F);
            UIHelper.drawFloatingText(IOStr[2], renderPos, 0.5F, -0.4F, 0.8F, 0F, 0F, 180F);
            UIHelper.drawFloatingText(IOStr[3], renderPos, 1.4F, 0.5F, 0.8F, 0F, 0F, 270F);

            UIHelper.drawRectangle(x, y, z + 0.9, x - 0.1, y + 0.35, z + 0.9, 255, 255, 255, 150);
            UIHelper.drawRectangle(x, y + 0.65, z + 0.9, x - 0.1, y + 1.0, z + 0.9, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.1, y, z + 0.9, x + 1.0, y + 0.35, z + 0.9, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.1, y + 0.65, z + 0.9, x + 1.0, y + 1.0, z + 0.9, 255, 255, 255, 150);

            UIHelper.drawRectangle(x + 0.35, y - 0.1, z + 0.9, x - 0.1, y, z + 0.9, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.10, y - 0.1, z + 0.9, x + 0.65, y, z + 0.9, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 0.35, y + 1.0, z + 0.9, x - 0.1, y + 1.1, z + 0.9, 255, 255, 255, 150);
            UIHelper.drawRectangle(x + 1.1, y + 1.0, z + 0.9, x + 0.65, y + 1.1, z + 0.9, 255, 255, 255, 150);
            break;

        case WEST:
            UIHelper.drawFloatingText(IOStr[0], renderPos, 0.2F, 0.5F, 1.4F, 180F, 90F, 90F);
            UIHelper.drawFloatingText(IOStr[1], renderPos, 0.2F, 1.4F, 0.5F, 180F, 90F, 180F);
            UIHelper.drawFloatingText(IOStr[2], renderPos, 0.2F, 0.5F, -0.4F, 180F, 90F, 270F);
            UIHelper.drawFloatingText(IOStr[3], renderPos, 0.2F, -0.4F, 0.5F, 180F, 90F, 0F);

            UIHelper.drawRectangleEW(x + 0.1, y + 0.35, z - 0.1, x + 0.1, y, z, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.1, y + 1.0, z - 0.1, x + 0.1, y + 0.65, z, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.1, y + 0.35, z + 1.0, x + 0.1, y, z + 1.1, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.1, y + 1.0, z + 1.0, x + 0.1, y + 0.65, z + 1.1, 255, 255, 255, 150);

            UIHelper.drawRectangleEW(x + 0.1, y, z - 0.1, x + 0.1, y - 0.1, z + 0.35, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.1, y, z + 0.65, x + 0.1, y - 0.1, z + 1.1, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.1, y + 1.1, z - 0.1, x + 0.1, y + 1.0, z + 0.35, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.1, y + 1.1, z + 0.65, x + 0.1, y + 1.0, z + 1.1, 255, 255, 255, 150);
            break;

        case EAST:
            UIHelper.drawFloatingText(IOStr[0], renderPos, 0.8F, 0.5F, 1.4F, 0F, 90F, 90F);
            UIHelper.drawFloatingText(IOStr[1], renderPos, 0.8F, -0.4F, 0.5F, 0F, 90F, 180F);
            UIHelper.drawFloatingText(IOStr[2], renderPos, 0.8F, 0.5F, -0.4F, 0F, 90F, 270F);
            UIHelper.drawFloatingText(IOStr[3], renderPos, 0.8F, 1.4F, 0.5F, 0F, 90F, 0F);

            UIHelper.drawRectangleEW(x + 0.9, y, z - 0.1, x + 0.9, y + 0.35, z, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.9, y + 0.65, z - 0.1, x + 0.9, y + 1.0, z, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.9, y, z + 1.0, x + 0.9, y + 0.35, z + 1.1, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.9, y + 0.65, z + 1.0, x + 0.9, y + 1.0, z + 1.1, 255, 255, 255, 150);

            UIHelper.drawRectangleEW(x + 0.9, y - 0.1, z - 0.1, x + 0.9, y, z + 0.35, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.9, y - 0.1, z + 0.65, x + 0.9, y, z + 1.1, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.9, y + 1.0, z - 0.1, x + 0.9, y + 1.1, z + 0.35, 255, 255, 255, 150);
            UIHelper.drawRectangleEW(x + 0.9, y + 1.0, z + 0.65, x + 0.9, y + 1.1, z + 1.1, 255, 255, 255, 150);
            break;

        default:
            break;
        }
    }

}
