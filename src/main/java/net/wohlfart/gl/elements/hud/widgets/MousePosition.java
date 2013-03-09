package net.wohlfart.gl.elements.hud.widgets;

import net.wohlfart.gl.input.CommandEvent;

import com.google.common.eventbus.Subscribe;



/**
 * <p>MousePosition class.</p>
 *
 *
 *
 */
public class MousePosition extends FormattedLabel {

    /**
     * <p>Constructor for MousePosition.</p>
     *
     * @param x a int.
     * @param y a int.
     */
    public MousePosition(int x, int y) {
        super(x, y, "Mouse Position: {0, number, ###},{1, number, ###}");
    }

    /**
     * <p>onMouseMove.</p>
     *
     * @param positionEvent a {@link net.wohlfart.gl.input.CommandEvent.PositionPointer} object.
     */
    @Subscribe
    public synchronized void onMouseMove(CommandEvent.PositionPointer positionEvent) {
        setValue(new Object[] {positionEvent.getX(), positionEvent.getY()});
    }

}
