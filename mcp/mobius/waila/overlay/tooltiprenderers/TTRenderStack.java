package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.Dimension;

/**
 * Custom renderer for item stacks.
 * Syntax : {waila.stack, type, id, amount, meta, haseffects}
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
        boolean hasEffects = Boolean.parseBoolean(params[4]);

        ItemStack stack = null;
        if (id > 0) {
            if (type == 0)
                stack = new ItemStack(Block.blocksList[id], amount, meta);
            else if (type == 1)
                stack = new ItemStack(Item.itemsList[id], amount, meta);
        }
        if (hasEffects && stack != null)
            stack.addEnchantment(Enchantment.unbreaking, 1); // any enchantment adds back effect

        DisplayUtil.renderStack(x, y, stack);
    }

}
