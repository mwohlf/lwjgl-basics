package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL20;



// handlers for matrices used in the shader
public enum UniformHandle {
	MODEL_TO_WORLD(IShaderProgram.MODEL_MATRIX),
	WORLD_TO_CAM(IShaderProgram.VIEW_MATRIX),
	CAM_TO_CLIP(IShaderProgram.PROJECTION_MATRIX);

	private final String lookupString;

	UniformHandle(final String lookupString) {
		this.lookupString = lookupString;
	}

	public int getLocation(final ShaderProgram shaderProgram) {
		return GL20.glGetUniformLocation(shaderProgram.getProgramId(), lookupString);
	}


}
