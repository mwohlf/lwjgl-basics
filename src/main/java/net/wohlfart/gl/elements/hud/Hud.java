package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;

public class Hud implements Renderable {


	private IGraphicContext graphicContext;

	private final Layer layerOne = new Layer();

	public void init(IGraphicContext graphicContext) {
		this.graphicContext = graphicContext;
	}


	@Override
	public void render() {
		Matrix4f camViewMatrix = GraphicContextManager.INSTANCE.getProjectionMatrix();
		// Matrix4f rotMatrix = SimpleMatrix4f.create(avatar.getRotation());

		GraphicContextManager.INSTANCE.setCurrentGraphicContext(graphicContext);
		ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
		ShaderUniformHandle.WORLD_TO_CAM.set(SimpleMath.UNION_MATRIX);
		ShaderUniformHandle.CAM_TO_CLIP.set(camViewMatrix);

		layerOne.render();

	}

	@Override
	public void dispose() {

	}

}
