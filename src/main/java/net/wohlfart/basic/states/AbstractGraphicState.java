package net.wohlfart.basic.states;

import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.view.Camera;

import org.lwjgl.opengl.Display;

import com.google.common.eventbus.Subscribe;

abstract class AbstractGraphicState implements GameState {

    private final GraphicContextManager graphContextManager = GraphicContextManager.INSTANCE;
    private InputDispatcher inputDispatcher;
    private final Camera camera = new Camera();
    private boolean quit = false;


    @Override
    public void setup() {
        inputDispatcher = graphContextManager.getInputDispatcher();
        inputDispatcher.register(camera);
        inputDispatcher.register(this);
    }

    protected  void setCurrentGraphicContext(IGraphicContext graphicContext) {
        graphContextManager.setCurrentGraphicContext(graphicContext);
    }

    protected float getScreenHeight() {
        return graphContextManager.getScreenHeight();
    }

    protected float getScreenWidth() {
        return graphContextManager.getScreenWidth();
    }

    protected InputDispatcher getInputDispatcher() {
        return inputDispatcher;
    }

    protected Camera getCamera() {
        return camera;
    }
    /**
     * <p>This method is called by the event bus on exit.
     * You have to register this class in order to get notified.</p>
     *
     * @param exitEvent a {@link net.wohlfart.gl.input.CommandEvent.Exit} object.
     */
    @Subscribe
    public synchronized void onExitTriggered(CommandEvent.Exit exitEvent) {
        quit = true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDone() {
        return Display.isCloseRequested() || quit;
    }

    @Override
    public void destroy() {
        inputDispatcher.unregister(this);
        inputDispatcher.unregister(camera);
    }

}
