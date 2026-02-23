package mcp.mobius.waila.overlay.tooltiprenderers;

import mcp.mobius.waila.api.ICommonAccessor;
import mcp.mobius.waila.api.ITooltipRenderer;
import mcp.mobius.waila.api.SpecialChars;
import mcp.mobius.waila.overlay.DisplayUtil;
import net.minecraft.src.Block;
import net.minecraft.src.Enchantment;
import net.minecraft.src.Item;
import net.minecraft.src.ItemStack;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.Dimension;

/**
 * Custom renderer for item stacks.
 * Syntax: {waila.stack, type, id, amount, meta, haseffects, w, h}
 */
public class TTRenderStack implements ITooltipRenderer {

    private static final int DEFAULT_W = 18;
    private static final int DEFAULT_H = 18;

    @Override
    public Dimension getSize(String[] params, ICommonAccessor accessor) {
        int w = params.length > 6 ? Integer.parseInt(params[5]) : DEFAULT_W;
        int h = params.length > 6 ? Integer.parseInt(params[6]) : DEFAULT_H;
        return new Dimension(w, h);
    }

    @Override
    public void draw(String[] params, ICommonAccessor accessor, int x, int y) {
        int type = Integer.parseInt(params[0]); //0 for block, 1 for item
        int id = Integer.parseInt(params[1]);
        int amount = Integer.parseInt(params[2]);
        int meta = Integer.parseInt(params[3]);
        boolean hasEffects = Boolean.parseBoolean(params[4]);
        int w = params.length > 6 ? Integer.parseInt(params[5]) : DEFAULT_W;
        int h = params.length > 6 ? Integer.parseInt(params[6]) : DEFAULT_H;

        ItemStack stack = null;
        if (id > 0) {
            if (type == 0)
                stack = new ItemStack(Block.blocksList[id], amount, meta);
            else if (type == 1)
                stack = new ItemStack(Item.itemsList[id], amount, meta);
        }
        if (hasEffects && stack != null)
            stack.addEnchantment(Enchantment.unbreaking, 1); // any enchantment adds back effect

        if (w != DEFAULT_W || h != DEFAULT_H) {
            GL11.glPushMatrix();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glScaled((double) w / DEFAULT_W, (double) h / DEFAULT_H, 1.0f);

            DisplayUtil.renderStack(0, 0, stack);

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            GL11.glPopMatrix();
        } else {
            DisplayUtil.renderStack(x, y, stack);
        }
    }

    public static String create(ItemStack stack, int w, int h) {
        boolean empty = stack == null;
        int id = empty ? 0 : stack.getItem().shiftedIndex;
        return SpecialChars.getRenderString("waila.stack",
                1, id, empty ? 1 : stack.stackSize, empty ? 0 : stack.getItemDamage(),
                !empty && stack.isItemEnchanted(), w, h);
    }

    public static String create(ItemStack stack) {
        return create(stack, DEFAULT_W, DEFAULT_H);
    }

}
