package net.wohlfart.gl.elements.skybox;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextHolder.IGraphicContext;
import net.wohlfart.gl.shader.PerspectiveProjectionFab;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.view.Camera;
import net.wohlfart.tools.SimpleMath;

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

    private final Matrix4f camViewMatrix = new Matrix4f();


    private BoxSide[] sides;

    private IGraphicContext graphicContext;


    @Override
    public void setup() {
        graphicContext = new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER);
        graphicContext.setup();
        camViewMatrix.load(new PerspectiveProjectionFab().create(CONTEXT_HOLDER.getSettings()));
    }

    private void createSides() {
        sides = BoxSide.values();
        for (final BoxSide side : sides) {
            side.setup(this);
        }
    }

    @Override
    public void render() {
        assert graphicContext != null : "the graphicContext is null, make sure to call the init method";
        assert graphicContext.isInitialized() : "the graphicContext is not initialized";

        CONTEXT_HOLDER.setCurrentGraphicContext(graphicContext);
        if (sides == null) {
            createSides();
        }

        final Camera camera = CONTEXT_HOLDER.getCamera();
        SimpleMath.convert(camera.getRotation(), rotMatrix);

        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(rotMatrix);
        ShaderUniformHandle.CAM_TO_CLIP.set(camViewMatrix);

        // draw only the visible sides
        camera.getForward(viewDirection);
        viewDirection.normalise(viewDirection);
        for (final BoxSide side : sides) {
            if (Vector3f.dot(viewDirection, side.getNormal()) > BoxSide.DOT_PROD_LIMIT) {
                side.render();
            }
        }
    }

    @Override
    public void destroy() {
        sides = null;
    }

    @Override
    public PerlinNoiseParameters getNoiseParamClouds() {
        return clouds;
    }

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
