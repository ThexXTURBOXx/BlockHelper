package mcp.mobius.waila.gui.widgets;

import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.gui.interfaces.IWidget;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Point;

public class PictureDisplay extends WidgetBase {

	protected String texture;

	public PictureDisplay(IWidget parent, String uri){
		super(parent);
		this.texture = uri;
	}

	@Override
	public void draw(Point pos) {
		this.saveGLState();

		GL11.glPushMatrix();
		this.renderEngine.bindTexture(texture);
		UIHelper.drawTexture(pos.getX(), pos.getY(), this.getSize().getX(), this.getSize().getY());
		GL11.glPopMatrix();

		this.loadGLState();
	}
}
