package net.wohlfart.gl.elements.hud;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.gl.elements.hud.widgets.CharAtlas;
import net.wohlfart.gl.elements.hud.widgets.CharAtlasBuilder;
import net.wohlfart.gl.elements.hud.widgets.TextComponent;
import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.mesh.IMesh;

import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// see: http://www.java-gaming.org/index.php?topic=25516.0
// read: http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
// and: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
// and: http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2005.html
class LayerImpl implements Layer {
    protected static final Logger LOGGER = LoggerFactory.getLogger(LayerImpl.class);

    protected final Collection<Renderable> components = new HashSet<Renderable>();

    protected IMesh meshData;

    protected CharAtlas characterAtlas;

    protected IMesh setup() {
        characterAtlas = new CharAtlasBuilder().build();
        final TextureMeshBuilder builder = new TextureMeshBuilder();
        builder.setTextureId(characterAtlas.getTextureId());
        builder.setTranslation(new Vector3f(0,-0.5f,-0.9f));
        return builder.build();
    }

    @Override
    public void render() {
        if (meshData == null) {
            meshData = setup();
        }
        // meshData.draw();
        for (final Renderable component : components) {
            component.render();
        }
    }

    @Override
    public void dispose() {
        meshData.dispose();
        meshData = null;
    }

    public void add(TextComponent label) {
        label.setLayer(this); // double dispatch
        components.add(label);
    }

    @Override
    public CharAtlas getCharacterAtlas() {
        return characterAtlas;
    }

}
