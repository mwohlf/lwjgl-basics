package net.wohlfart.gl.elements.hud.widgets;

import net.wohlfart.gl.input.CommandEvent;

import com.google.common.eventbus.Subscribe;

/**
 * <p>
 * MousePositionLabel class.
 * </p>
 */
public class MousePositionLabel extends FormattedLabel {

    /**
     * <p>
     * Constructor for MousePositionLabel.
     * </p>
     * 
     * @param x
     *            a int.
     * @param y
     *            a int.
     */
    public MousePositionLabel(int x, int y) {
        super(x, y, "Mouse Position: {0, number, ###},{1, number, ###}");
    }

    /**
     * <p>
     * onMouseMove.
     * </p>
     * 
     * @param positionEvent
     *            a {@link net.wohlfart.gl.input.CommandEvent.PositionPointer} object.
     */
    @Subscribe
    public synchronized void onMouseMove(CommandEvent positionEvent) {
        // TODO: fix this
        // setValue(new Object[] {positionEvent.getX(), positionEvent.getY()});
    }

}
