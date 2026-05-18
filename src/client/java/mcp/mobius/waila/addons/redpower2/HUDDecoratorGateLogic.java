package mcp.mobius.waila.addons.redpower2;

import java.util.HashMap;
import java.util.Map;
import mcp.mobius.waila.api.IBlockDecorator;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.WailaDirection;
import mcp.mobius.waila.gui.helpers.UIHelper;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Vec3D;

public final class HUDDecoratorGateLogic implements IBlockDecorator {

    public static final IBlockDecorator INSTANCE = new HUDDecoratorGateLogic();

    public static final Map<String, byte[]> IOARRAYS = new HashMap<String, byte[]>();

    static {
        IOARRAYS.put("Timer", new byte[]{6, 2, 2, 2});
        IOARRAYS.put("Sequencer", new byte[]{2, 2, 2, 2});
        IOARRAYS.put("IRLatch", new byte[]{2, 1, 2, 1});
        IOARRAYS.put("IRNor", new byte[]{1, 1, 2, 1});
        IOARRAYS.put("IROr", new byte[]{1, 1, 2, 1});
        IOARRAYS.put("IRNand", new byte[]{1, 1, 2, 1});
        IOARRAYS.put("IRAnd", new byte[]{1, 1, 2, 1});
        IOARRAYS.put("IRXnor", new byte[]{0, 1, 2, 1});
        IOARRAYS.put("IRXor", new byte[]{0, 1, 2, 1});
        IOARRAYS.put("IRPulse", new byte[]{1, 0, 2, 0});
        IOARRAYS.put("IRToggle", new byte[]{2, 1, 2, 1});
        IOARRAYS.put("IRNot", new byte[]{1, 2, 2, 2});
        IOARRAYS.put("IRBuf", new byte[]{1, 2, 2, 2});
        IOARRAYS.put("IRMux", new byte[]{3, 4, 2, 5});
        IOARRAYS.put("IRCounter", new byte[]{2, 8, 2, 7});
        IOARRAYS.put("IRRepeater", new byte[]{1, 0, 2, 0});
        IOARRAYS.put("RPArray", new byte[]{11, 11, 11, 11});
        IOARRAYS.put("RPInv", new byte[]{11, 2, 11, 2});
        IOARRAYS.put("RPNonInv", new byte[]{11, 2, 11, 2});
    }

    static final String[] IONAMES = {"", "IN", "OUT", "SWAP", "IN_A", "IN_B", "LOCK",
            "POS", "NEG", "BUS", "EN", "IO", "RST"};

    static final int[] LABEL_ROT_OFFS = new int[]{1, 3, 2, 2, 3, 1};

    private HUDDecoratorGateLogic() {
    }

    @Override
    public void decorateBlock(ItemStack itemStack, IDataAccessor accessor, IPluginConfig config) {
        if (!config.get("pr.showio")) return;

        int rawRot = accessor.getNBTInteger("rot");
        int side = (rawRot >> 2) & 0x7; // Side on block
        int front = rawRot & 0x3; // Orientation
        byte[] ioArray = IOARRAYS.get(accessor.getNBTData().getString("id"));
        if (side >= 6 || ioArray == null) return;

        Vec3D renderPos = accessor.getRenderingPosition();
        double x = renderPos.xCoord;
        double y = renderPos.yCoord;
        double z = renderPos.zCoord;

        WailaDirection orient = WailaDirection.getOrientation(side);

        String[] IOStr = new String[4];
        int rot = front + LABEL_ROT_OFFS[side];
        for (int i = 0; i < 4; i++) {
            IOStr[i] = IONAMES[ioArray[(i - rot + 16) % 4]];
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
