package net.wohlfart.gl.shader;


import org.lwjgl.util.vector.Matrix4f;



/**
 * wrapping a GraphicContext, if we ever need to switch the GraphicContext
 */
public enum GraphicContextManager {
	INSTANCE;

	private GraphicContext currentGraphicContext;

	// the projection matrix defines the lens of the camera, e.g. view angle
	private Matrix4f projectionMatrix;

	public void setCurrentGraphicContext(GraphicContext graphicContext) {
		this.currentGraphicContext = graphicContext;
	}

	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}


	// package private only called by ShaderAttributeHandle
	int getLocation(ShaderAttributeHandle shaderAttributeHandle) {
		return currentGraphicContext.getLocation(shaderAttributeHandle);
	}

	// package private only called by ShaderUniformHandle
	int getLocation(ShaderUniformHandle shaderUniformHandle) {
		return currentGraphicContext.getLocation(shaderUniformHandle);
	}

}
