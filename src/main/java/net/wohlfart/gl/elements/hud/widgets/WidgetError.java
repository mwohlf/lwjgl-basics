package net.wohlfart.gl.elements.hud.widgets;

@SuppressWarnings("serial")
public class WidgetError extends RuntimeException {

    WidgetError(String message, Throwable exception) {
        super(message, exception);
    }

}
