package mcp.mobius.waila.gui.widgets.movable;

import mcp.mobius.waila.gui.events.MouseEvent;
import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.gui.interfaces.Signal;
import mcp.mobius.waila.gui.widgets.WidgetBase;
import mcp.mobius.waila.utils.GLState;
import net.minecraft.src.ModLoader;
import org.lwjgl.util.Point;


// A movable picture setup especially for centered relative positions.

public class PictureMovableRC extends WidgetBase {

    private double offsetX, offsetY;
    protected String texture;

    public PictureMovableRC(IWidget parent, String uri) {
        super(parent);
        this.texture = uri;
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        this.offsetX = event.x - this.geom.getUnalignedPos(this.parent).getX();
        this.offsetY = event.y - this.geom.getUnalignedPos(this.parent).getY();
    }

    @Override
    public void onMouseDrag(MouseEvent event) {
        double newX = event.x - this.offsetX;
        double newY = event.y - this.offsetY;

        newX = Math.max(newX, this.parent.getLeft());
        newY = Math.max(newY, this.parent.getTop());

        newX = Math.min(newX, this.parent.getRight());
        newY = Math.min(newY, this.parent.getBottom());

        this.setPos(((newX - this.parent.getLeft()) / this.parent.getSize().getX()) * 100.0,
                ((newY - this.parent.getTop()) / this.parent.getSize().getY()) * 100.0);

        this.emit(Signal.DRAGGED, this.getPos());
    }

    @Override
    public void draw(Point pos) {
        GLState state = new GLState();

        ModLoader.getMinecraftInstance().renderEngine.bindTexture(
                ModLoader.getMinecraftInstance().renderEngine.getTexture(texture));
        UIHelper.drawTexture(pos.getX(), pos.getY(), this.getSize().getX(), this.getSize().getY());

        state.reset();
    }

}
