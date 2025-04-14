package mcp.mobius.waila.gui.widgets;

import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.utils.GLState;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class PictureDisplay extends WidgetBase {

    protected String texture;

    public PictureDisplay(IWidget parent, String uri) {
        super(parent);
        this.texture = uri;
    }

    @Override
    public void draw(Point pos) {
        GLState state = new GLState();

        GL11.glPushMatrix();
        this.renderEngine.bindTexture(texture);
        UIHelper.drawTexture(pos.getX(), pos.getY(), this.getSize().getX(), this.getSize().getY());
        GL11.glPopMatrix();

        state.reset();
    }

}
