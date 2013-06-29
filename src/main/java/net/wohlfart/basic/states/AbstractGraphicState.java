package net.wohlfart.basic.states;

import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.view.Camera;

import org.lwjgl.opengl.Display;

import com.google.common.eventbus.Subscribe;

abstract class AbstractGraphicState implements GameState {

    private final GraphicContextManager graphContextManager = GraphicContextManager.INSTANCE;
    private boolean quit = false;
    private Camera camera;

    @Override
    public void setup() {
        graphContextManager.register(camera);
        graphContextManager.register(this);
    }

    protected float getScreenHeight() {
        return graphContextManager.getScreenHeight();
    }

    protected float getScreenWidth() {
        return graphContextManager.getScreenWidth();
    }

    protected Camera getCamera() {
        return camera;
    }

    // spring bean injected
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    protected GraphicContextManager getGraphContextManager() {
        return graphContextManager;
    }


    /**
     * <p>
     * This method is called by the event bus on exit. You have to register this class in order to get notified.
     * </p>
     *
     * @param exitEvent
     *            a {@link net.wohlfart.gl.input.CommandEvent.Exit} object.
     */
    @Subscribe
    public synchronized void onExitTriggered(CommandEvent exitEvent) {
        quit = true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDone() {
        return Display.isCloseRequested() || quit;
    }

    @Override
    public void destroy() {
        graphContextManager.unregister(this);
        graphContextManager.unregister(camera);
    }

}
