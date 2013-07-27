package net.wohlfart.basic.states;

import net.wohlfart.basic.hud.Hud;
import net.wohlfart.basic.hud.HudImpl;
import net.wohlfart.basic.hud.widgets.SimpleLabel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * Default implementation of a game state, only contains a hud
 */
final class SimpleState extends AbstractGraphicState implements InitializingBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleState.class);

    private final Hud hud = new HudImpl();


    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("<afterPropertiesSet>");
        Assert.notNull(hud);
    }

    @Override
    public void setup() {
        super.setup();
        hud.setup();
        hud.add(new SimpleLabel(0, 0, "hello world at (0,0)"));
    }

    @Override
    public void update(float tpf) {
        hud.update(tpf);
    }

    @Override
    public void render() {
        hud.render();
    }

    @Override
    public void destroy() {
        hud.destroy();
        super.destroy();
    }

}
