package net.wohlfart.gl.elements.hud;

import net.wohlfart.gl.renderer.Renderable;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.GraphicContextManager.IGraphicContext;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;

public class Hud implements Renderable {

	private IGraphicContext hudContext;

	private LayerImpl textLayer;

	public void init(IGraphicContext hudContext) {
		this.hudContext = hudContext;
		this.textLayer = new LayerImpl();
	}


	public void add(Label label) {
		textLayer.add(label);
	}

	@Override
	public void render() {
		Matrix4f camViewMatrix = GraphicContextManager.INSTANCE.getProjectionMatrix();
		GraphicContextManager.INSTANCE.setCurrentGraphicContext(hudContext);
		ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
		ShaderUniformHandle.WORLD_TO_CAM.set(SimpleMath.UNION_MATRIX);
		ShaderUniformHandle.CAM_TO_CLIP.set(camViewMatrix);
		GL11.glEnable(GL11.GL_BLEND);
		textLayer.render();
	}

	@Override
	public void dispose() {
		textLayer.dispose();
	}

}
