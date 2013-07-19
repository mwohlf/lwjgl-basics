package net.wohlfart.basic.container;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.basic.elements.IsUpdatable;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextHolder.IGraphicContext;
import net.wohlfart.gl.shader.PerspectiveProjectionFab;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.shader.VertexLight;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;

/**
 * implementing the basic features for a render set,
 * keeping the graphic context and some lights if needed
 */
public class DefaultRenderBatch<T extends IsUpdatable> implements RenderBatch<T>, IsUpdatable { // REVIEWED

    private IGraphicContext graphicContext;

    private final HashSet<T> elements = new HashSet<T>();
    private final HashSet<VertexLight> lights = new HashSet<VertexLight>(10);

    private final Matrix4f projMatrix = new Matrix4f(); // view with ratio for the settings properties


    public void setGraphicContext(IGraphicContext graphicContext) {
        this.graphicContext = graphicContext;
    }

    public void add(VertexLight light) {
        lights.add(light);
    }

    public void add(T element) {
        elements.add(element);
    }

    public void addAll(Collection<T> multipleElements) {
        elements.addAll(multipleElements);
    }

    public HashSet<T> getElements() {
        return elements;
    }

    @Override
    public void setup() {
        projMatrix.load(new PerspectiveProjectionFab().create(CONTEXT_HOLDER.getSettings()));
        if (graphicContext == null) { // fallback
            graphicContext = new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER);
        }
        graphicContext.setup();
    }

    @Override
    public void render() {
        assert graphicContext != null : "the graphicContext is null";
        assert graphicContext.isInitialized() : "the graphicContext is not initialized, call setup() before using the graphicContext";

        CONTEXT_HOLDER.setCurrentGraphicContext(graphicContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX); // the default, each model should set its own later
        ShaderUniformHandle.WORLD_TO_CAM.set(CONTEXT_HOLDER.getCamera().getWorldToCamMatrix());
        ShaderUniformHandle.CAM_TO_CLIP.set(projMatrix);

        int i = 0;
        for (final VertexLight light : lights) {
            ShaderUniformHandle.LIGHTS.set(light, i++);
        }

        for (T element : elements) {
            element.render();
        }
    }

    @Override
    public void update(float timeInSec) {
        for (final T element : elements) {
            element.update(timeInSec);
        }
    }

    @Override
    public void destroy() {
        for (final T element : elements) {
            element.destroy();
        }
        elements.clear();
    }

}
