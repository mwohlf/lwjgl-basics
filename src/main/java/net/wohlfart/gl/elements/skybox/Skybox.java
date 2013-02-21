package net.wohlfart.gl.elements.skybox;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Skybox implements Renderable, SkyboxParameters {


    private final Vector3f viewDirection = new Vector3f();

    private final Matrix4f rotMatrix = new Matrix4f();

    private BoxSideMesh[] sides;

    private Avatar avatar;

    private IGraphicContext graphicContext;

    public void init(IGraphicContext graphicContext, Avatar avatar) {
        this.avatar = avatar;
        this.graphicContext = graphicContext;
        // in order to build the mesh we need the unions and attribute positions, so we have to switch
        // to the gfx context used for rendering, maybe we could wait for the render call...
        GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
        final BoxSide[] values = BoxSide.values();
        final List<BoxSideMesh> array = new ArrayList<BoxSideMesh>(values.length);
        for (final BoxSide side : values) {
            array.add(side.build(this));
        }
        sides = array.toArray(new BoxSideMesh[values.length]);
    }

    @Override
    public void render() {
        assert sides != null : "the skybox sides are null, make sure to call the init method";
        assert avatar != null : "the avatar is null, make sure to call the init method";
        assert graphicContext != null : "the graphicContext is null, make sure to call the init method";

        avatar.readDirection(viewDirection);
        viewDirection.normalise(viewDirection);

        final Matrix4f camViewMatrix = GraphicContextManager.INSTANCE.getProjectionMatrix();
        SimpleMath.convert(avatar.getRotation(), rotMatrix);

        GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(rotMatrix);
        ShaderUniformHandle.CAM_TO_CLIP.set(camViewMatrix);

        GL11.glDisable(GL11.GL_BLEND);

        // draw only the visible sides
        for (final BoxSideMesh side : sides) {
            if (Vector3f.dot(viewDirection, side.getNormal()) > BoxSide.DOT_PROD_LIMIT) {
                side.draw();
            }
        }
    }

    @Override
    public void dispose() {
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

}
