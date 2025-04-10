package mcp.mobius.waila.overlay.tooltiprenderers;

import java.awt.Dimension;
import mcp.mobius.waila.api.IWailaCommonAccessor;
import mcp.mobius.waila.api.IWailaTooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TTRenderStack implements IWailaTooltipRenderer {

    @Override
    public Dimension getSize(String[] params, IWailaCommonAccessor accessor) {
        return new Dimension(16, 16);
    }

    @Override
    public void draw(String[] params, IWailaCommonAccessor accessor) {
        int type = Integer.parseInt(params[0]); //0 for block, 1 for item
        int id = Integer.parseInt(params[1]); //ID
        int amount = Integer.parseInt(params[2]);
        int meta = Integer.parseInt(params[3]);

        ItemStack stack = null;
        if (type == 0)
            stack = new ItemStack(Block.blocksList[id], amount, meta);
        if (type == 1)
            stack = new ItemStack(Item.itemsList[id], amount, meta);

        RenderHelper.enableGUIStandardItemLighting();
        DisplayUtil.renderStack(0, 0, stack);
        RenderHelper.disableStandardItemLighting();
    }

}
