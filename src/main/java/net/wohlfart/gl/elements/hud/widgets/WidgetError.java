package net.wohlfart.gl.elements.hud.widgets;

public class WidgetError extends RuntimeException {

    WidgetError(String message, Throwable exception) {
        super(message, exception);
    }

}
