package net.wohlfart.gl.shader;

import java.util.Arrays;

import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * modeling an OpenGL Context:
 *  - shader
 *  - attribute location
 *  - uniform locations
 *
 * see: http://www.opengl.org/wiki/OpenGL_Context
 */
public class GraphicContext {
	protected static final Logger LOGGER = LoggerFactory.getLogger(GraphicContext.class);

	private final IShaderProgram shaderProgram;

	private final int[] attributeMap = new int[ShaderAttributeHandle.values().length];
	private final int[] matrixMap = new int[ShaderUniformHandle.values().length];


	public GraphicContext(IShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		init();
	}


	// read uniforms and attribute locations and buffer them
	private void init() {

		for (ShaderAttributeHandle attributeHandle : ShaderAttributeHandle.values()) {
			//int location = attributeHandle.getLocation(shaderProgram);
			int location = GL20.glGetAttribLocation(shaderProgram.getProgramId(), attributeHandle.getLookupString());
			LOGGER.debug("attributeMap lookup: '{}({})' to '{}'",
					new Object[] {attributeHandle.name(), attributeHandle.ordinal(), location});
			if (location < 0) {
				LOGGER.warn("location for AttributeHandle '{}' is '{}' wich is <0, the programId is '{}'",
						new Object[] {attributeHandle, location, shaderProgram.getProgramId()});
			}
			attributeMap[attributeHandle.ordinal()] = location;
		}
		LOGGER.debug("attributeMap setup: '{}'", Arrays.toString(attributeMap));


		for (ShaderUniformHandle matrixHandle : ShaderUniformHandle.values()) {
			//int location = matrixHandle.getLocation(shaderProgram);
			int location = GL20.glGetUniformLocation(shaderProgram.getProgramId(), matrixHandle.getLookupString());
			LOGGER.debug("matrixMap lookup: '{}({})' to '{}'",
					new Object[] {matrixHandle.name(), matrixHandle.ordinal(), location});
			if (location < 0) {
				LOGGER.warn("location for UniformHandle '{}' is '{}' wich is <0, the programId is '{}'",
						new Object[] {matrixHandle, location, shaderProgram.getProgramId()});
			}
			matrixMap[matrixHandle.ordinal()] = location;
		}
		LOGGER.debug("matrixMap setup: '{}'", Arrays.toString(matrixMap));

	}

	public int getLocation(ShaderAttributeHandle shaderAttributeHandle) {
		return attributeMap[shaderAttributeHandle.ordinal()];
	}

	public int getLocation(ShaderUniformHandle shaderUniformHandle) {
		return matrixMap[shaderUniformHandle.ordinal()];
	}

}
