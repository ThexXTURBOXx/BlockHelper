package mcp.mobius.waila.gui.widgets;

import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.utils.GLState;
import net.minecraft.src.ScaledResolution;
import org.lwjgl.opengl.GL11;

public class LayoutCropping extends LayoutBase {

    int xOffset = 0;
    int yOffset = 0;

    public LayoutCropping(IWidget parent) {
        super(parent);
    }

    public void setOffsets(int xoffset, int yoffset) {
        this.xOffset = xoffset;
        this.yOffset = yoffset;
    }

    @Override
    public void draw() {
        this.rez = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        GLState state = new GLState();

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, this.alpha);

        this.draw(this.getPos());

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(this.getPos().getX() * this.rez.scaleFactor,
                (this.rez.getScaledHeight() - (this.getPos().getY() + this.getSize().getY())) * this.rez.scaleFactor,
                this.getSize().getX() * this.rez.scaleFactor, this.getSize().getY() * this.rez.scaleFactor);

        GL11.glTranslatef(xOffset, yOffset, 0.0f);

        for (IWidget widget : this.renderQueue_LOW.values())
            if (widget.shouldRender())
                widget.draw();

        for (IWidget widget : this.renderQueue_MEDIUM.values())
            if (widget.shouldRender())
                widget.draw();

        for (IWidget widget : this.renderQueue_HIGH.values())
            if (widget.shouldRender())
                widget.draw();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        state.reset();
    }

    @Override
    public IWidget getWidgetAtCoordinates(double posX, double posY) {
        return super.getWidgetAtCoordinates(posX - xOffset, posY - yOffset);
    }

}
