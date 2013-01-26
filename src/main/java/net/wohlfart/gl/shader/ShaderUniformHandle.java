package net.wohlfart.gl.shader;

import java.nio.FloatBuffer;

import org.lwjgl.opengl.GL20;



// handlers for matrices used in the shader
public enum ShaderUniformHandle {
	MODEL_TO_WORLD(IShaderProgram.MODEL_MATRIX),
	WORLD_TO_CAM(IShaderProgram.VIEW_MATRIX),
	CAM_TO_CLIP(IShaderProgram.PROJECTION_MATRIX);


	final String lookupString;

	ShaderUniformHandle(final String lookupString) {
		this.lookupString = lookupString;
	}

	/*
	// remove this method and get the vvalue from the Context
	public int getLocation(final IShaderProgram shaderProgram) {
		return GL20.glGetUniformLocation(shaderProgram.getProgramId(), lookupString);
	}
	*/

	public void setValue(final FloatBuffer colorBuffer) {
		GL20.glUniform4(GraphicContextManager.INSTANCE.getLocation(this), colorBuffer);
	}

	public int getLocation() {
		return GraphicContextManager.INSTANCE.getLocation(this);
	}

}
