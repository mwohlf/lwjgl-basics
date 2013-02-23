package net.wohlfart.basic.states;

import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.Statistics;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.DefaultShaderProgram;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

class SimpleState implements GameState {
    protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleState.class);

    private boolean quit = false;

    private GraphicContextManager.IGraphicContext defaultGraphicContext;
    private GraphicContextManager.IGraphicContext wireframeGraphicContext;
    private GraphicContextManager.IGraphicContext hudGraphicContext;

    protected GraphicContextManager graphContext = GraphicContextManager.INSTANCE;

    private final Avatar avatar = new Avatar();

    private final Skybox skybox = new Skybox();
    private final RenderBucket elemBucket = new RenderBucket();
    private final Hud hud = new Hud();

    private final boolean skyboxOn = false;
    private final boolean elementsOn = true;
    private final boolean hudOn = true;


    // data for the render loop:
    private final Vector3f posVector = new Vector3f();
    private final Matrix4f posMatrix = new Matrix4f();
    private final Matrix4f rotMatrix = new Matrix4f();
    private final Matrix4f rotPosMatrix = new Matrix4f();


    private Statistics statistics;
    private InputDispatcher inputDispacther;

    @Override
    public void setup() {
        inputDispacther = graphContext.getInputDispatcher();
        statistics = new Statistics(0, 0);


        // event bus registration
        inputDispacther.register(avatar);
        inputDispacther.register(this);


        wireframeGraphicContext = new DefaultGraphicContext(
                new DefaultShaderProgram(DefaultShaderProgram.WIREFRAME_VERTEX_SHADER, DefaultShaderProgram.WIREFRAME_FRAGMENT_SHADER));
        defaultGraphicContext = new DefaultGraphicContext(
                new DefaultShaderProgram(DefaultShaderProgram.DEFAULT_VERTEX_SHADER, DefaultShaderProgram.DEFAULT_FRAGMENT_SHADER));
        hudGraphicContext = new DefaultGraphicContext(
                new DefaultShaderProgram(DefaultShaderProgram.HUD_VERTEX_SHADER, DefaultShaderProgram.HUD_FRAGMENT_SHADER));

        if (skyboxOn) {
            skybox.init(defaultGraphicContext, avatar);
        }

        if (elementsOn) {
            //elemBucket.add(new ElementCreator().createRandomElements());
            elemBucket.add(new ElementCreator().createCircles());
        }

        if (hudOn) {
            hud.init(hudGraphicContext);
            hud.add(statistics);
        }

    }


    @Subscribe
    public synchronized void onExitTriggered(CommandEvent.Exit exitEvent) {
        quit = true;
    }

    @Subscribe
    public synchronized void onMouseMove(CommandEvent.PositionPointer positionEvent) {
        int x = positionEvent.getX();
        int y = positionEvent.getY();
    }

    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);

        if (hudOn) {
            statistics.update(tpf);
        }

        //System.out.println("Avatar: " + avatar);

        // todo:
        // move the models / actors / perform the actions
    }

    @Override
    public void render() {

        if (skyboxOn) {
            skybox.render();
        }

        if (elementsOn) {
            SimpleMath.convert(avatar.getPosition().negate(posVector), posMatrix);
            SimpleMath.convert(avatar.getRotation(), rotMatrix);
            Matrix4f.mul(rotMatrix, posMatrix, rotPosMatrix);

            GraphicContextManager.INSTANCE.setCurrentGraphicContext(wireframeGraphicContext);
            ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
            ShaderUniformHandle.WORLD_TO_CAM.set(rotPosMatrix);
            ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextManager.INSTANCE.getProjectionMatrix());
            elemBucket.render();
        }

        if (hudOn) {
            hud.render();
        }

    }

    @Override
    public boolean isDone() {
        return Display.isCloseRequested() || quit;
    }

    @Override
    public void dispose() {
        defaultGraphicContext.dispose();
        wireframeGraphicContext.dispose();
        // event bus unregistration
        inputDispacther.unregister(this);
        inputDispacther.unregister(avatar);
    }

}
