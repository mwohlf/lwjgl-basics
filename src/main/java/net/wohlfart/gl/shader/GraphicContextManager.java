package net.wohlfart.gl.shader;


import net.wohlfart.gl.input.DefaultInputDispatcher;
import net.wohlfart.gl.input.InputDispatcher;

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

	protected static final Logger LOGGER = LoggerFactory.getLogger(GraphicContextManager.class);


	public interface IGraphicContext {

		int getLocation(ShaderAttributeHandle shaderAttributeHandle);

		int getLocation(ShaderUniformHandle shaderUniformHandle);

		void bind();

		void unbind();

		void dispose();

	}


	private IGraphicContext currentGraphicContext;

	// the projection matrix defines the lens of the camera, e.g. view angle
	private Matrix4f projectionMatrix;

	private InputDispatcher inputDispatcher;

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

	// package private for ShaderAttributeHandle and ShaderUniformHandle
	IGraphicContext getCurrentGraphicContext() {
		return currentGraphicContext;
	}



	public void setProjectionMatrix(Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}

	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}


	public void setInputDispatcher(DefaultInputDispatcher inputSource) {
		this.inputDispatcher = inputSource;
	}

	public InputDispatcher getInputDispatcher() {
		return inputDispatcher;
	}

}
