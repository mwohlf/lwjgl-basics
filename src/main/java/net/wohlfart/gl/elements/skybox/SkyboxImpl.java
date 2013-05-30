package net.wohlfart.gl.elements.skybox;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.view.CanRotate;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>
 * SkyboxImpl class.
 * </p>
 */
public class SkyboxImpl implements Skybox, SkyboxParameters {

    private final Vector3f viewDirection = new Vector3f();

    private final Matrix4f rotMatrix = new Matrix4f();

    private BoxSideMesh[] sides;

    private CanRotate camera;

    private IGraphicContext graphicContext;

    public void setGraphicContext(IGraphicContext graphicContext) {
        this.graphicContext = graphicContext;
    }

    public void setCamera(CanRotate camera) {
        this.camera = camera;
    }

    @Override
    public void setup() {
        graphicContext.setup();
    }

    private void createSides() {
        final BoxSide[] values = BoxSide.values();
        final List<BoxSideMesh> newSides = new ArrayList<BoxSideMesh>(values.length);
        for (final BoxSide side : values) {
            newSides.add(side.build(this));
        }
        sides = newSides.toArray(new BoxSideMesh[values.length]);
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
        assert graphicContext != null : "the graphicContext is null, make sure to call the init method";
        assert camera != null : "the camera is null, make sure to call the init method";

        GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
        if (sides == null) {
            createSides();
        }

        final Matrix4f camViewMatrix = GraphicContextManager.INSTANCE.getPerspectiveProjMatrix();
        SimpleMath.convert(camera.getRotation(), rotMatrix);

        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(rotMatrix);
        ShaderUniformHandle.CAM_TO_CLIP.set(camViewMatrix);

        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glDisable(GL11.GL_BLEND);

        // draw only the visible sides
        camera.getForward(viewDirection);
        viewDirection.normalise(viewDirection);
        for (final BoxSideMesh side : sides) {
            if (Vector3f.dot(viewDirection, side.getNormal()) > BoxSide.DOT_PROD_LIMIT) {
                side.render();
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
        sides = null;
    }

    /** {@inheritDoc} */
    @Override
    public PerlinNoiseParameters getNoiseParamClouds() {
        return clouds;
    }

    /** {@inheritDoc} */
    @Override
    public PerlinNoiseParameters getNoiseParamStars() {
        return stars;
    }

    private final PerlinNoiseParameters clouds = new PerlinNoiseParameters() {

        @Override
        public int getOctaves() {
            return 5;
        }

        @Override
        public float getPersistence() {
            return 0.5f;
        }

        @Override
        public float getFrequency() {
            return 1f;
        }

        @Override
        public float getW() {
            return 0.5f;
        }

    };

    private final PerlinNoiseParameters stars = new PerlinNoiseParameters() {

        @Override
        public int getOctaves() {
            return 5;
        }

        @Override
        public float getPersistence() {
            return 0.7f;
        }

        @Override
        public float getFrequency() {
            return 3f;
        }

        @Override
        public float getW() {
            return 1;
        }

    };

    @Override
    public void dispose() {
        //
    }

}
