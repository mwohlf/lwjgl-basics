package net.wohlfart.gl.shader;


import org.lwjgl.util.vector.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * wrapping a GraphicContext, if we ever need to switch the GraphicContext
 *
 * todo: implement stuff from:
 * http://www.lwjgl.org/wiki/index.php?title=GLSL_Utility_Class
 */
public enum GraphicContextManager {
	INSTANCE;

	public interface IGraphicContext {

		int getLocation(ShaderAttributeHandle shaderAttributeHandle);

		int getLocation(ShaderUniformHandle shaderUniformHandle);

		void bind();

		void unbind();

		void dispose();

	}


	protected static final Logger LOGGER = LoggerFactory.getLogger(GraphicContextManager.class);

	private IGraphicContext currentGraphicContext;

	// the projection matrix defines the lens of the camera, e.g. view angle
	private Matrix4f projectionMatrix;

	public void setCurrentGraphicContext(IGraphicContext graphicContext) {
		LOGGER.debug("setting gfx context to '{}'", graphicContext);

		if (currentGraphicContext != null) {
			currentGraphicContext.unbind();
		}
		currentGraphicContext = graphicContext;
		if (currentGraphicContext != null) {
			currentGraphicContext.bind();
		}
	}


	// can also be set with a uniform handle
	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}


	IGraphicContext getCurrentGraphicContext() {
		return currentGraphicContext;
	}

	/*
	int getLocation(ShaderAttributeHandle shaderAttributeHandle) {
		return currentGraphicContext.getLocation(shaderAttributeHandle);
	}

	int getLocation(ShaderUniformHandle shaderUniformHandle) {
		return currentGraphicContext.getLocation(shaderUniformHandle);
	}
	*/

}
