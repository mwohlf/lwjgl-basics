package net.wohlfart.gl.elements.hud;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.gl.elements.hud.widgets.AbstractTextComponent;
import net.wohlfart.gl.elements.hud.widgets.CharAtlas;
import net.wohlfart.gl.elements.hud.widgets.CharAtlasBuilder;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.GraphicContextManager;

import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// see: http://www.java-gaming.org/index.php?topic=25516.0
// read: http://stackoverflow.com/questions/10617589/why-would-it-be-beneficial-to-have-a-separate-projection-matrix-yet-combine-mod
// and: http://www.arcsynthesis.org/gltut/Positioning/Tut07%20The%20Perils%20of%20World%20Space.html
// and: http://www.arcsynthesis.org/gltut/Positioning/Tutorial%2005.html
class LayerImpl implements Layer {
    /** Constant <code>LOGGER</code> */
    protected static final Logger LOGGER = LoggerFactory.getLogger(LayerImpl.class);

    private final GraphicContextManager cxtManager = GraphicContextManager.INSTANCE;

    protected final Collection<IsRenderable> components = new HashSet<IsRenderable>();

    protected IsRenderable meshData;

    protected CharAtlas characterAtlas;

    /**
     * <p>setup.</p>
     *
     * @return a {@link net.wohlfart.gl.shader.mesh.IRenderable} object.
     */
    protected IsRenderable setup() {
        characterAtlas = new CharAtlasBuilder().build();
        final TextureMeshBuilder builder = new TextureMeshBuilder();
        builder.setTextureId(characterAtlas.getTextureId());
        float z = cxtManager.getNearPlane() - 1;
        builder.setTranslation(new Vector3f(0,-0.5f,z));
        return builder.build();
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        if (meshData == null) {
            meshData = setup();
        }
        //meshData.draw();
        for (final IsRenderable component : components) {
            component.render();
        }
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        meshData.destroy();
        meshData = null;
    }

    /**
     * <p>add.</p>
     *
     * @param label a {@link net.wohlfart.gl.elements.hud.widgets.AbstractTextComponent} object.
     */
    public void add(AbstractTextComponent label) {
        label.setLayer(this); // double dispatch
        components.add(label);
    }

    /** {@inheritDoc} */
    @Override
    public CharAtlas getCharacterAtlas() {
        return characterAtlas;
    }

}
