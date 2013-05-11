package net.wohlfart.basic.states;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.gl.elements.hud.Hud;
import net.wohlfart.gl.elements.hud.widgets.Label;
import net.wohlfart.gl.elements.hud.widgets.MousePositionLabel;
import net.wohlfart.gl.elements.hud.widgets.Statistics;
import net.wohlfart.gl.elements.skybox.Skybox;
import net.wohlfart.gl.input.InputDispatcher;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.renderer.RenderableBucket;
import net.wohlfart.gl.view.MousePicker;
import net.wohlfart.tools.ControllerFrame;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;


/*
 * state implementation that consists of (in the order of rendering):
 * - skyboxImpl
 * - elementBucket
 * - hudImpl
 *
 */
final class SimpleState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleState.class);

    private Statistics statistics;
    private MousePositionLabel mousePositionLabel;
    private MousePicker mousePicker;

    private final boolean controlFrameOn = true;

    private Skybox skybox;
    private RenderableBucket elemBucket;
    private Hud hud;


    public void setSkybox(Skybox skybox) {
        this.skybox = skybox;
    }

    public void setElemBucket(RenderableBucket elemBucket) {
        this.elemBucket = elemBucket;
    }

    public void setHud(Hud hud) {
        this.hud = hud;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(skybox, "skybox missing, you probably forgot to inject skybox in the configs");
        Assert.notNull(elemBucket, "elemBucket missing, you probably forgot to inject elemBucket in the configs");
        Assert.notNull(hud, "hud missing, you probably forgot to inject hud in the configs");
    }


    /** {@inheritDoc} */
    @Override
    public void setup() {
        super.setup();

        skybox.setup();
        elemBucket.setup();
        hud.setup();

        statistics = new Statistics(0, -40);
        mousePositionLabel = new MousePositionLabel(0, -20);
        mousePicker = new MousePicker(elemBucket, getScreenWidth(), getScreenHeight());

        // FIXME: move this into sprng config
        @SuppressWarnings("serial")
        Collection<IsRenderable> collection = new HashSet<IsRenderable>() {{
            addAll(SceneCreator.createCircledTarget());
            addAll(SceneCreator.createRandomLocatedSpheres());
            addAll(SceneCreator.createRandomElements());
            addAll(SceneCreator.createOriginAxis());
            addAll(SceneCreator.createDebugElements());
            add(new ControllerFrame().init().getCube());
        }};

        elemBucket.setContent(collection);


        // event bus registration
        InputDispatcher inputDispatcher = getInputDispatcher();
        inputDispatcher.register(mousePositionLabel);
        inputDispatcher.register(mousePicker);


        if (controlFrameOn) {

        }

        hud.add(statistics);
        hud.add(mousePositionLabel);
        hud.add(new Label(0, 0, "hello world at (0,0)"));

    }



    /** {@inheritDoc} */
    @Override
    public void update(float tpf) {
        LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f / tpf);
        statistics.update(tpf);
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        skybox.render();
        elemBucket.render();
        hud.render();
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        skybox.dispose();
        elemBucket.dispose();
        hud.dispose();
        super.destroy();
    }

}
