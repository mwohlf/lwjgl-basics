package net.wohlfart.basic.states;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.view.Camera;

import org.lwjgl.opengl.Display;
import org.springframework.util.Assert;

import com.google.common.eventbus.Subscribe;


/**
 * Base class for a game state, the setup() method must be called before this class or any subclass can be used,
 * the following features are implemented:
 * - handle input event registration
 * - handle the camera instance
 * - checking for quit and close events
 */
abstract class AbstractGraphicState implements GameState { // REVIEWED

    private boolean quit = false;
    private Camera camera;


    @Override
    abstract public void update(float tpf);

    // spring bean injected
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void setup() {
        Assert.notNull(camera);
        CONTEXT_HOLDER.register(this);
    }

    protected float getScreenHeight() {
        return CONTEXT_HOLDER.getSettings().getHeight();
    }

    protected float getScreenWidth() {
        return CONTEXT_HOLDER.getSettings().getWidth();
    }

    protected Camera getCamera() {
        return camera;
    }

    @Subscribe // called by the event bus
    public synchronized void onExitTriggered(CommandEvent exitEvent) {
        quit = true;
    }

    @Override
    public boolean isDone() {
        return Display.isCloseRequested() || quit;
    }

    @Override
    public void destroy() {
        CONTEXT_HOLDER.unregister(this);
    }

}
