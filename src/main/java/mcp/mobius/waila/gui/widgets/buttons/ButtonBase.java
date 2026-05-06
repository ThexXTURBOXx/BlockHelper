package mcp.mobius.waila.gui.widgets.buttons;

import mcp.mobius.waila.gui.events.MouseEvent;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.gui.interfaces.Signal;
import mcp.mobius.waila.gui.widgets.LabelFixedFont;
import mcp.mobius.waila.gui.widgets.WidgetBase;
import mcp.mobius.waila.overlay.DisplayUtil;
import mcp.mobius.waila.utils.GLState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public abstract class ButtonBase extends WidgetBase {

    protected static final int BUTTON_TEX_U = 0;
    protected static final int BUTTON_TEX_V_BASE = 66;
    protected static final int BUTTON_TEX_V_STEP = 20;
    protected static final int BUTTON_TEX_W = 200;
    protected static final int BUTTON_TEX_H = 20;
    protected static final int BUTTON_BORDER = 2;
    protected static final int COLOR_TEXT = 0xe0e0e0;
    protected static final int COLOR_TEXT_HOVER = 0xffffa0;
    protected static final int COLOR_TEXT_DISABLED = 0xffa0a0a0;

    protected static final String WIDGETS_TEXTURE = "/gui/gui.png";

    protected boolean enabled = true;
    protected boolean mouseOver = false;

    public ButtonBase(IWidget parent) {
        super(parent);
    }

    @Override
    public void draw() {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int textColor = !enabled ? COLOR_TEXT_DISABLED : this.mouseOver ? COLOR_TEXT_HOVER : COLOR_TEXT;

        for (IWidget widget : this.widgets.values()) {
            if (!(widget instanceof LabelFixedFont)) continue;
            LabelFixedFont label = (LabelFixedFont) widget;
            label.setShadow(true);
            label.setColor(textColor);
        }

        super.draw();
    }

    @Override
    public void draw(Point pos) {
        GLState state = new GLState();
        this.drawVanillaButton(this.enabled, this.mouseOver);
        state.reset();
    }

    protected void drawVanillaButton(boolean enabled, boolean mouseOver) {
        this.drawVanillaButton(!enabled ? -1 : mouseOver ? 1 : 0);
    }

    protected void drawVanillaButton(int texOffset) {
        int width = this.getSize().getX();
        int height = this.getSize().getY();
        if (width <= 0 || height <= 0) return;

        int x = this.getPos().getX();
        int y = this.getPos().getY();
        int v = BUTTON_TEX_V_BASE + texOffset * BUTTON_TEX_V_STEP;

        this.mc.renderEngine.bindTexture(WIDGETS_TEXTURE);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        DisplayUtil.drawTexturedModalRect(x, y,
                0, v,
                width / 2, height);
        DisplayUtil.drawTexturedModalRect(x + width / 2, y,
                BUTTON_TEX_W - width / 2, v,
                width / 2, height);
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
            this.mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);

        this.emit(Signal.CLICKED, event.button);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
