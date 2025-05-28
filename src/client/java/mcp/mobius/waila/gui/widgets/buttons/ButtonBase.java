package mcp.mobius.waila.gui.widgets.buttons;

import mcp.mobius.waila.gui.events.MouseEvent;
import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.gui.interfaces.Signal;
import mcp.mobius.waila.gui.widgets.LabelFixedFont;
import mcp.mobius.waila.gui.widgets.WidgetBase;
import mcp.mobius.waila.utils.GLState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public abstract class ButtonBase extends WidgetBase {

    protected static final String WIDGETS_TEXTURE = "/gui/gui.png";

    protected boolean mouseOver = false;

    public ButtonBase(IWidget parent) {
        super(parent);
    }

    @Override
    public void draw() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        for (IWidget widget : this.widgets.values())
            if (widget instanceof LabelFixedFont)
                ((LabelFixedFont) widget).setColor(this.mouseOver ? 0xffffa0 : 0xffffff);

        super.draw();
    }

    @Override
    public void draw(Point pos) {
        GLState state = new GLState();

        this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture(WIDGETS_TEXTURE));
        int texOffset = this.mouseOver ? 1 : 0;
        UIHelper.drawTexture(this.getPos().getX(), this.getPos().getY(), this.getSize().getX(), this.getSize().getY(),
                0, 66 + texOffset * 20, 200, 20);

        state.reset();
    }

    @Override
    public void onMouseEnter(MouseEvent event) {
        mouseOver = true;
    }

    @Override
    public void onMouseLeave(MouseEvent event) {
        mouseOver = false;
    }

    @Override
    public IWidget getWidgetAtCoordinates(double posX, double posY) {
        return this;
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        if (event.button == 0)
            this.mc.sndManager.func_337_a("random.click", 1.0F, 1.0F);

        this.emit(Signal.CLICKED, event.button);
    }

}
