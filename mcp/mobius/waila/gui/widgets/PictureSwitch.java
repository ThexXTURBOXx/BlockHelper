package mcp.mobius.waila.gui.widgets;

import mcp.mobius.waila.gui.events.MouseEvent;
import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.utils.GLState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class PictureSwitch extends WidgetBase {

    private final String texture1;
    private final String texture2;
    private boolean mouseOver = false;

    public PictureSwitch(IWidget parent, String uri1, String uri2) {
        super(parent);
        this.texture1 = uri1;
        this.texture2 = uri2;
    }

    @Override
    public void draw(Point pos) {
        GLState state = new GLState();

        String texture = mouseOver ? this.texture2 : this.texture1;

        GL11.glPushMatrix();
        this.renderEngine.bindTexture(texture);
        UIHelper.drawTexture(pos.getX(), pos.getY(), this.getSize().getX(), this.getSize().getY());
        GL11.glPopMatrix();

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

}
