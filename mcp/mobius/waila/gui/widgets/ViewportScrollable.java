package mcp.mobius.waila.gui.widgets;

import mcp.mobius.waila.gui.events.MouseEvent;
import mcp.mobius.waila.gui.helpers.UIHelper;
import mcp.mobius.waila.gui.interfaces.CType;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.gui.interfaces.Signal;
import mcp.mobius.waila.gui.interfaces.WAlign;
import org.lwjgl.util.Point;

public class ViewportScrollable extends WidgetBase {

    public static class Escalator extends WidgetBase {

        int yOffset = 0;
        int sizeCursor = 8;
        int maxValue = 0;
        int step;
        boolean drag = false;

        public Escalator(IWidget parent, int step) {
            this.parent = parent;
            this.step = step;
        }

        public void addOffset(int yoffset) {
            setOffset(this.yOffset + yoffset);
        }

        public void setOffset(int yoffset) {
            this.yOffset = Math.min(Math.max(yoffset, this.maxValue), 0);
            this.emit(Signal.VALUE_CHANGED, this.yOffset);
        }

        public void setMaxValue(int value) {
            this.maxValue = value;
        }

        public void setStep(int step) {
            this.step = step;
        }

        @Override
        public void draw(Point pos) {
            UIHelper.drawGradientRect(this.getLeft(), this.getTop(), this.getRight(), this.getBottom(), 1,
                    0xff999999, 0xff999999);
            int offsetScaled =
                    (int) (((double) this.getSize().getY() - (double) sizeCursor + 1) / (double) this.maxValue * yOffset);
            UIHelper.drawGradientRect(this.getLeft(), this.getTop() + offsetScaled, this.getRight(),
                    this.getTop() + offsetScaled + sizeCursor, 1, 0xffffffff, 0xffffffff);
        }

        @Override
        public void onMouseClick(MouseEvent event) {
            if (event.button == 0) {
                int offsetScaled =
                        this.getTop() + (int) (((double) this.getSize().getY() - (double) sizeCursor + 1) / (double) this.maxValue * (yOffset));

                this.drag = false;

                int newOffset = 0;
                if (event.y < offsetScaled)
                    newOffset = this.step;
                else if (event.y > offsetScaled + sizeCursor)
                    newOffset = -this.step;
                else
                    this.drag = true;

                this.addOffset(newOffset);
            } else {
                super.onMouseClick(event);
            }
        }

        @Override
        public void onMouseRelease(MouseEvent event) {
            if (event.button == 0)
                this.drag = false;
            super.onMouseRelease(event);
        }

        @Override
        public void onMouseDrag(MouseEvent event) {
            if (this.drag) {
                int relativeY = (int) event.y - this.getTop();
                double factor = ((double) this.getSize().getY() - (double) sizeCursor + 1) / (double) this.maxValue;
                this.setOffset((int) (relativeY / factor));
                this.emit(Signal.DRAGGED, this);
            } else
                super.onMouseDrag(event);
        }

    }

    IWidget attachedWidget = null;
    int yOffset = 0;
    int step = 5;

    public ViewportScrollable(IWidget parent) {
        super(parent);
        this.addWidget("Cropping", new LayoutCropping(null)).setGeometry(new WidgetGeometry(0.0, 0.0,
                100.0, 100.0, CType.RELXY, CType.RELXY, WAlign.LEFT, WAlign.TOP));
        this.addWidget("Escalator", new Escalator(null, this.step * 5)).setGeometry(new WidgetGeometry(100.0, 0,
                8, 100.0, CType.RELXY, CType.REL_Y, WAlign.RIGHT, WAlign.TOP)).hide();
    }

    public IWidget attachWidget(IWidget widget) {
        this.attachedWidget = this.getWidget("Cropping").addWidget("Cropped", widget);
        return this.attachedWidget;
    }

    public IWidget getAttachedWidget() {
        return this.attachedWidget;
    }

    public IWidget setStep(int step) {
        this.step = step;
        ((Escalator) this.getWidget("Escalator")).setStep(this.step * 5);
        return this;
    }

    @Override
    public void draw(Point pos) {
    }

    @Override
    public void draw() {
        //if (Display.wasResized())

        if ((this.attachedWidget != null) && (this.attachedWidget.getSize().getY() > this.getSize().getY()))
            this.getWidget("Escalator").show();
        else
            this.getWidget("Escalator").hide();

        super.draw();
    }

    @Override
    public void onMouseWheel(MouseEvent event) {
        ((Escalator) this.getWidget("Escalator")).addOffset((int) (event.z / 120.0 * this.step));
    }

    @Override
    public void onWidgetEvent(IWidget srcwidget, Signal signal, Object... params) {
        if (srcwidget.equals(this.attachedWidget) && signal == Signal.GEOM_CHANGED)
            ((Escalator) this.getWidget("Escalator")).setMaxValue(this.getSize().getY() - srcwidget.getSize().getY());
        else if (srcwidget.equals(this.getWidget("Escalator")) && signal == Signal.VALUE_CHANGED)
            ((LayoutCropping) this.getWidget("Cropping")).setOffsets(0, (Integer) params[0]);
        else
            super.onWidgetEvent(srcwidget, signal, params);
    }

    @Override
    public void onMouseDrag(MouseEvent event) {
        if (this.getWidget("Escalator").shouldRender() && ((Escalator) this.getWidget("Escalator")).drag)
            this.getWidget("Escalator").onMouseDrag(event);
        else
            super.onMouseDrag(event);
    }

    @Override
    public void onMouseClick(MouseEvent event) {
        if (event.button == 0) {
            if (this.getWidget("Escalator").isWidgetAtCoordinates(event.x, event.y)) {
                this.getWidget("Escalator").onMouseClick(event);
            } else {
                ((Escalator) this.getWidget("Escalator")).drag = false;
                super.onMouseClick(event);
            }
        } else
            super.onMouseClick(event);
    }

}
