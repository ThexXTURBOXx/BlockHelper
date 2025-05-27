package mcp.mobius.waila.gui.widgets;

import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.utils.GLState;
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

        this.mc.renderEngine.bindTexture(this.mc.renderEngine.getTexture(texture));
        UIHelper.drawTexture(pos.getX(), pos.getY(), this.getSize().getX(), this.getSize().getY());

        state.reset();
    }

}
