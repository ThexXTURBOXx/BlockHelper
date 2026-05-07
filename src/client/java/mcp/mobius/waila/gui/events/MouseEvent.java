package mcp.mobius.waila.gui.events;

import mcp.mobius.waila.gui.interfaces.IWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.src.mod_BlockHelper;
import org.lwjgl.input.Mouse;

public class MouseEvent {

    public enum EventType {NONE, MOVE, CLICK, RELEASED, DRAG, WHEEL, ENTER, LEAVE}

    public static final int BUTTON_COUNT = Mouse.getButtonCount();

    public long timestamp;
    public Minecraft mc;
    public IWidget srcwidget;
    public IWidget trgwidget;
    public double x, y;
    public int z;
    public boolean[] buttonState = new boolean[BUTTON_COUNT];
    public EventType type;
    public int button = -1;

    public MouseEvent(IWidget widget) {
        this.srcwidget = widget;
        this.timestamp = System.currentTimeMillis();

        this.mc = mod_BlockHelper.minecraft;

        this.x = (double) Mouse.getEventX() * (double) this.srcwidget.getSize().getX() / (double) this.mc.displayWidth;
        this.y = (double) this.srcwidget.getSize().getY() -
                 (double) Mouse.getEventY() * (double) this.srcwidget.getSize().getY() / (double) this.mc.displayHeight - 1.0;

        this.z = Mouse.getDWheel();

        for (int i = 0; i < BUTTON_COUNT; i++)
            buttonState[i] = Mouse.isButtonDown(i);

        this.trgwidget = this.srcwidget.getWidgetAtCoordinates(this.x, this.y);
    }

    public String toString() {
        StringBuilder retstring = new StringBuilder(String.format("MOUSE %s: [%s] [ %.2f %.2f %d ] [",
                this.type, this.timestamp, this.x, this.y, this.z));

        for (int i = 0; i < Math.min(5, BUTTON_COUNT); ++i)
            retstring.append(' ').append(this.buttonState[i]).append(' ');

        retstring.append(']');

        if (this.button != -1)
            retstring.append(" Button ").append(this.button);

        return retstring.toString();
    }

    // Returns the event type based on the previous mouse event.
    public EventType getEventType(MouseEvent me) {

        this.type = EventType.NONE;

        if (this.trgwidget != me.trgwidget) {
            this.type = EventType.ENTER;
            return this.type;
        }

        if (this.z != 0) {
            this.type = EventType.WHEEL;
            return this.type;
        }

        for (int i = 0; i < BUTTON_COUNT; i++) {
            if (this.buttonState[i] != me.buttonState[i]) {
                this.type = this.buttonState[i] ? EventType.CLICK : EventType.RELEASED;
                this.button = i;
                return this.type;
            }
        }

        //MOVE & DRAG EVENTS (we moved the mouse and button 0 was clicked or not)
        if ((this.x != me.x) || (this.y != me.y)) {
            this.type = this.buttonState[0] ? EventType.DRAG : EventType.MOVE;
            return this.type;
        }

        return this.type;
    }

}
