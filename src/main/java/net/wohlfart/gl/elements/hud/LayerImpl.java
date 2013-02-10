package net.wohlfart.gl.elements.hud;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.mesh.IMesh;
import net.wohlfart.tools.FontRenderer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// see: http://www.java-gaming.org/index.php?topic=25516.0
// read: http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
// and: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
// and: http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2005.html
public class LayerImpl implements Layer {
    protected static final Logger LOGGER = LoggerFactory.getLogger(LayerImpl.class);

    protected final Collection<Renderable> components = new HashSet<Renderable>();

    protected IMesh meshData;

    protected CharacterAtlas characterAtlas;

    protected IMesh setup() {
        characterAtlas = new FontRenderer().init().getCharacterAtlas();
        final LayerMeshBuilder builder = new LayerMeshBuilder();
        builder.setTextureId(characterAtlas.getTextureId());
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

    public void add(Label label) {
        label.setLayer(this); // double dispatch
        components.add(label);
    }

    public CharacterAtlas getCharacterAtlas() {
        return characterAtlas;
    }

}
