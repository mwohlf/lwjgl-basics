package net.wohlfart.gl.shader;

import net.wohlfart.basic.Settings;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.view.Camera;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//@formatter:off
/**
 * The GraphicContextHolder is responsible for
 * - switching between graphic contexts containing shaders
 * - holding the current graphic context and providing access to attributes and uniforms
 * - holding settings like screen dimension specified at startup
 * - calculating and holding the perspective- and orthographic-projection matrix
 * - holding the input dispatcher and handling registration and de-registration
 */
public enum GraphicContextHolder { // REVIEWED
    CONTEXT_HOLDER; // @formatter:on

    protected static final Logger LOGGER = LoggerFactory.getLogger(GraphicContextHolder.class);

    public interface IGraphicContext {

        int getAttributeLocation(String lookupString);

        int getUniformLocation(String lookupString);

        void setup();

        void bind();

        void unbind();

        void dispose();

        boolean isInitialized();

    }

    // current shader and stuff
    private IGraphicContext currentGraphicContext;

    private Settings settings;
    private InputDispatcher inputDispatcher;
    private Camera camera;


    public void setCurrentGraphicContext(IGraphicContext graphicContext) {
        LOGGER.debug("setting gfx context to '{}'", graphicContext);
        if (currentGraphicContext != null) {
            currentGraphicContext.unbind();
        }
        currentGraphicContext = graphicContext;
        if (currentGraphicContext != null) {
            currentGraphicContext.bind();
        }
    }


    public void setSettings(Settings settings) {
        this.settings = settings;
    }

    public void setCamera(Camera camera) {
        if (this.camera != null) {
            unregister(camera);
        }
        this.camera = camera;
        register(camera);
    }

    public Settings getSettings() {
        return this.settings;
    }

    int getAttributeLocation(String lookupString) {
        return currentGraphicContext.getAttributeLocation(lookupString);
    }

    int getUniformLocation(String lookupString) {
        return currentGraphicContext.getUniformLocation(lookupString);
    }

    public Camera getCamera() {
        return camera;
    }

    public void setInputDispatcher(InputDispatcher inputSource) {
        this.inputDispatcher = inputSource;
    }

    public void register(Object inputListener) {
        inputDispatcher.register(inputListener);
    }

    public void unregister(Object inputListener) {
        inputDispatcher.unregister(inputListener);
    }

    public void post(Object event) {
        inputDispatcher.post(event);
    }

    public void destroy() {
        unregister(camera);
    }
}
