package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DefaultShaderProgram extends AbstractShaderProgram {
	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultShaderProgram.class);

	private static final String VERTEX_SHADER_FILE = "/shaders/default/vertex.glsl";
	private static final String FRAGMENT_SHADER_FILE = "/shaders/default/fragment.glsl";

	private int vertexShaderId = -1;
	private int fragmentShaderId = -1;


	@Override
	public String toString() {
		return "DefaultShaderProgram loaded from: \n"
				+ VERTEX_SHADER_FILE + "\n"
				+ FRAGMENT_SHADER_FILE;
	}

	@Override
	public void setup() {
		super.setup();
		vertexShaderId = loadShader(VERTEX_SHADER_FILE, GL20.GL_VERTEX_SHADER);
		fragmentShaderId = loadShader(FRAGMENT_SHADER_FILE, GL20.GL_FRAGMENT_SHADER);
		linkAndValidate(vertexShaderId, fragmentShaderId);
	}

	@Override
	public void dispose() {
		unlink(vertexShaderId, fragmentShaderId);
		super.dispose();
	}

}
