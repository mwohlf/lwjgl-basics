package net.wohlfart.gl.elements;

import java.util.ArrayList;
import java.util.List;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;
import net.wohlfart.tools.SimpleMatrix4f;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class Skybox implements Renderable, SkyboxParameters {

	private static final int SIZE = 1024;

	private static final float DOT_PROD_LIMIT = -0.1f; // this depends on the view angle

	private final Vector3f viewDirection = new Vector3f();

	private MeshWithNormal[] sides;

	private Avatar avatar;

	private IGraphicContext graphicContext;



	public void init(Avatar avatar, IGraphicContext graphicContext) {
		this.avatar = avatar;
		this.graphicContext = graphicContext;
		// in order to build the mesh we need the unions and attribute positions, so we have to switch
		// to the gfx context used for rendering, maybe we could wait for the render call...
		GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
		SkyboxSide[] values = SkyboxSide.values();
		List<MeshWithNormal> array = new ArrayList<MeshWithNormal>(values.length);
		for (SkyboxSide side : values) {
			array.add(side.build(this));
		}
		sides = array.toArray(new MeshWithNormal[values.length]);
	}

	@Override
	public int getSize() {
		return SIZE;
	}

	@Override
	public void render() {
		assert sides != null: "the skybox sides are null, make sure to call the init method";
		assert avatar != null: "the avatar is null, make sure to call the init method";
		assert graphicContext != null: "the graphicContext is null, make sure to call the init method";

		avatar.readDirection(viewDirection);

		Matrix4f camViewMatrix = GraphicContextManager.INSTANCE.getProjectionMatrix();
		Matrix4f rotMatrix = SimpleMatrix4f.create(avatar.getRotation());

		GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
		ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
		ShaderUniformHandle.WORLD_TO_CAM.set(rotMatrix);
		ShaderUniformHandle.CAM_TO_CLIP.set(camViewMatrix);

		// draw only the visible sides
		for (MeshWithNormal side : sides) {
			if (Vector3f.dot(viewDirection, side.getNormal()) > DOT_PROD_LIMIT) {
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
