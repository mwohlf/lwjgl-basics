package net.wohlfart.gl.elements.hud.widgets;

import net.wohlfart.gl.input.CommandEvent;

import com.google.common.eventbus.Subscribe;



public class MousePosition extends FormattedLabel {

    public MousePosition(int x, int y) {
        super(x, y, "Mouse Position: {0, number, ###},{1, number, ###}");
    }

    @Subscribe
    public synchronized void onMouseMove(CommandEvent.PositionPointer positionEvent) {
        setValue(new Object[] {positionEvent.getX(), positionEvent.getY()});
    }

}
