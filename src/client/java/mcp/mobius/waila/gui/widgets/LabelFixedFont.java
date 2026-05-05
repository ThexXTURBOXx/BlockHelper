package mcp.mobius.waila.gui.widgets;

import mcp.mobius.waila.gui.interfaces.CType;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.utils.GLState;
import mcp.mobius.waila.utils.I18n;
import org.lwjgl.util.Point;

public class LabelFixedFont extends WidgetBase {

    protected String text = "";
    protected int color;
    protected boolean shadow;

    public LabelFixedFont(IWidget parent, String text) {
        super(parent);
        this.setText(text);
        this.color = 0xFFFFFF;
        this.shadow = false;
    }

    public LabelFixedFont(IWidget parent, String text, int color) {
        super(parent);
        this.setText(text);
        this.color = color;
        this.shadow = false;
    }

    @Override
    public IWidget setGeometry(WidgetGeometry geom) {
        this.geom = geom;
        this.updateGeometry();
        return this;
    }

    public void setText(String text) {
        this.text = I18n.translate(text);
        this.updateGeometry();
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setShadow(boolean shadow) {
        this.shadow = shadow;
    }

    private void updateGeometry() {
        if (this.geom == null)
            this.geom = new WidgetGeometry(0, 0, 50, 50, CType.ABSXY, CType.ABSXY);

        this.geom = new WidgetGeometry(this.geom.x, this.geom.y, this.mc.fontRenderer.getStringWidth(this.text), 8,
                this.geom.posType, CType.ABSXY, this.geom.alignX, this.geom.alignY);
    }

    @Override
    public void draw(Point pos) {
        GLState state = new GLState();
        if (this.shadow) this.mc.fontRenderer.drawStringWithShadow(this.text, pos.getX(), pos.getY(), this.color);
        else this.mc.fontRenderer.drawString(this.text, pos.getX(), pos.getY(), this.color);
        state.reset();
    }
}
