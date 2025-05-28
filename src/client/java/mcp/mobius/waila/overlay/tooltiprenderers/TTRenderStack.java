package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.src.Block;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.lwjgl.util.Dimension;

/**
 * Custom renderer for item stacks.
 * Syntax: {waila.stack, type, id, amount, meta}
 */
public class TTRenderStack implements ITooltipRenderer {

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        return new Dimension(18, 18);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        int type = Integer.parseInt(params[0]); //0 for block, 1 for item
        int id = Integer.parseInt(params[1]);
        int amount = Integer.parseInt(params[2]);
        int meta = Integer.parseInt(params[3]);

        ItemStack stack = null;
        if (id > 0) {
            if (type == 0)
                stack = new ItemStack(Block.blocksList[id].blockID, amount, meta);
            else if (type == 1)
                stack = new ItemStack(Item.itemsList[id].shiftedIndex, amount, meta);
        }

        DisplayUtil.renderStack(x, y, stack);
    }

}
