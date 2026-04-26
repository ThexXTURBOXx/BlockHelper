package mcp.mobius.waila.gui.widgets;

import mcp.mobius.waila.gui.interfaces.CType;
import mcp.mobius.waila.gui.interfaces.IWidget;
import mcp.mobius.waila.gui.interfaces.WAlign;
import net.minecraft.src.MathHelper;
import org.lwjgl.util.Point;


public class WidgetGeometry {

    double x;
    double y;
    double sx;
    double sy;

    CType posType;
    CType sizeType;

    boolean fracPosX = false;
    boolean fracPosY = false;
    boolean fracSizeX = false;
    boolean fracSizeY = false;

    WAlign alignX;
    WAlign alignY;

    public static class PointDouble {

        double x;
        double y;

        public PointDouble(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public double getX() {
            return this.x;
        }

        public double getY() {
            return this.y;
        }

        public String toString() {
            return String.format("PointDouble: %.5f %.5f", this.x, this.y);
        }

    }

    public WidgetGeometry(double x, double y, double sx, double sy, CType fracPos, CType fracSize) {
        this(x, y, sx, sy, fracPos, fracSize, WAlign.LEFT, WAlign.TOP);
    }

    public WidgetGeometry(double x, double y, double sx, double sy, CType fracPos, CType fracSize,
                          WAlign alignX, WAlign alignY) {
        this.x = x;
        this.y = y;
        this.sx = sx;
        this.sy = sy;
        this.posType = fracPos;
        this.sizeType = fracSize;

        switch (fracPos) {
        case REL_X:
            this.fracPosX = true;
            break;
        case RELXY:
            this.fracPosX = true;
            this.fracPosY = true;
            break;
        case REL_Y:
            this.fracPosY = true;
            break;
        default:
            break;

        }

        switch (fracSize) {
        case REL_X:
            this.fracSizeX = true;
            break;
        case RELXY:
            this.fracSizeX = true;
            this.fracSizeY = true;
            break;
        case REL_Y:
            this.fracSizeY = true;
            break;
        default:
            break;

        }

        this.alignX = alignX;
        this.alignY = alignY;
    }

    public void setPos(double x, double y, boolean fracX, boolean fracY) {
        this.x = x;
        this.y = y;
        this.fracPosX = fracX;
        this.fracPosY = fracY;
    }

    public void setSize(double sx, double sy, boolean fracSizeX, boolean fracSizeY) {
        this.sx = sx;
        this.sy = sy;
        this.fracSizeX = fracSizeX;
        this.fracSizeY = fracSizeY;
    }

    public PointDouble getRawPos() {
        return new PointDouble(this.x, this.y);
    }

    public Point getUnalignedPos(IWidget parent) {
        int x = -1;
        if (this.fracPosX && parent != null)
            x = MathHelper.ceiling_double_int(parent.getPos().getX() + parent.getSize().getX() * this.x / 100D);
        else if (!this.fracPosX)
            x = parent != null ? parent.getPos().getX() + (int) this.x : (int) this.x;

        int y = -1;
        if (this.fracPosY && parent != null)
            y = MathHelper.ceiling_double_int(parent.getPos().getY() + parent.getSize().getY() * this.y / 100D);
        else if (!this.fracPosY)
            y = parent != null ? parent.getPos().getY() + (int) this.y : (int) this.y;

        return new Point(x, y);
    }

    public Point getPos(IWidget parent) {
        int x = -1;
        if (this.fracPosX && parent != null)
            x = MathHelper.ceiling_double_int(parent.getPos().getX() + parent.getSize().getX() * this.x / 100D);
        else if (!this.fracPosX)
            x = parent != null ? parent.getPos().getX() + (int) this.x : (int) this.x;

        if (this.alignX == WAlign.CENTER)
            x -= this.getSize(parent).getX() / 2;
        else if (this.alignX == WAlign.RIGHT)
            x -= this.getSize(parent).getX() - 1;


        int y = -1;
        if (this.fracPosY && parent != null)
            y = MathHelper.ceiling_double_int(parent.getPos().getY() + parent.getSize().getY() * this.y / 100D);
        else if (!this.fracPosY)
            y = parent != null ? parent.getPos().getY() + (int) this.y : (int) this.y;

        if (this.alignY == WAlign.CENTER)
            y -= this.getSize(parent).getY() / 2;
        else if (this.alignY == WAlign.BOTTOM)
            y -= this.getSize(parent).getY() - 1;

        return new Point(x, y);
    }

    public Point getSize(IWidget parent) {
        int sx = this.fracSizeX
                ? MathHelper.ceiling_double_int(parent.getSize().getX() * this.sx / 100D)
                : (int) this.sx;

        int sy = this.fracSizeY
                ? MathHelper.ceiling_double_int(parent.getSize().getY() * this.sy / 100D)
                : (int) this.sy;

        return new Point(sx, sy);
    }

    public String toString() {
        return "Geometry: [" +
               this.x + " " + this.y + "] [" + this.sx + " " + this.sy + "] [" +
               this.posType + " " + this.sizeType + "] [" + this.alignX + " " + this.alignY + "]";
    }

}
