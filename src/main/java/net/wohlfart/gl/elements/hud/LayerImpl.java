package net.wohlfart.gl.elements.hud;

import java.util.HashSet;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.hud.Layer.Widget;
import net.wohlfart.gl.elements.hud.txt.CharAtlas;
import net.wohlfart.gl.elements.hud.txt.CharAtlasBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// see: http://www.java-gaming.org/index.php?topic=25516.0
// read: http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
// and: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
// and: http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2005.html
@SuppressWarnings("serial")
class LayerImpl extends HashSet<Widget> implements Layer {

    protected static final Logger LOGGER = LoggerFactory.getLogger(LayerImpl.class);

    protected CharAtlas characterAtlas;

    @Override
    public void render() {
        for (final IsRenderable component : this) {
            component.render();
        }
    }

    @Override
    public void destroy() {
    }

    @Override
    public boolean add(Widget label) {
        label.setLayer(this); // double dispatch
        return super.add(label);
    }

    @Override
    public CharAtlas getCharacterAtlas() {
        return characterAtlas;
    }

    @Override
    public void update(float tpf) {
        if (characterAtlas == null) {
            characterAtlas = new CharAtlasBuilder()
                .setFontSize(12)
                //.setBorderOn(true)
                .build();
        }
        for (final Widget component : this) {
            component.update(tpf);
        }
    }

}
