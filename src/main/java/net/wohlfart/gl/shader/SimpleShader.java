package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public class SimpleShader extends AbstractShader{
	// variable names inside the vertex shader
	private static final String PROJECTION_MATRIX = "projectionMatrix";
	private static final String VIEW_MATRIX = "viewMatrix";
	private static final String MODEL_MATRIX = "modelMatrix";

	private static final String POSITION = "position";
	private static final String COLOR = "color";
	private static final String TEXTURE_COORD = "textureCoord";

	private static final String VERTEX_SHADER_FILE = "/shaders/simple/vertex.glsl";
	private static final String FRAGMENT_SHADER_FILE = "/shaders/simple/fragment.glsl";

	private int vertexShaderId;
	private int fragmentShaderId;
	private int programId;

	private int projectionMatrixLocation = -1;
	private int viewMatrixLocation = -1;
	private int modelMatrixLocation = -1;



	@Override
	public void init() {

		vertexShaderId = loadShader(VERTEX_SHADER_FILE, GL20.GL_VERTEX_SHADER);
		fragmentShaderId = loadShader(FRAGMENT_SHADER_FILE, GL20.GL_FRAGMENT_SHADER);

		// Create a new shader program that links both shaders
		programId = GL20.glCreateProgram();
		GL20.glAttachShader(programId, vertexShaderId);
		GL20.glAttachShader(programId, fragmentShaderId);
		GL20.glLinkProgram(programId);

		// Position information will be attribute 0
		GL20.glBindAttribLocation(programId, 0, POSITION);
		// Color information will be attribute 1
		GL20.glBindAttribLocation(programId, 1, COLOR);
		// Texture information will be attribute 2
		GL20.glBindAttribLocation(programId, 2, TEXTURE_COORD);

		// Get matrices uniform locations
		projectionMatrixLocation = GL20.glGetUniformLocation(programId, PROJECTION_MATRIX);
		viewMatrixLocation = GL20.glGetUniformLocation(programId, VIEW_MATRIX);
		modelMatrixLocation = GL20.glGetUniformLocation(programId, MODEL_MATRIX);

		GL20.glValidateProgram(programId);
	}

	@Override
	public void bind() {
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL20.glUseProgram(programId);

		uploadMatrices(projectionMatrix, projectionMatrixLocation);
		uploadMatrices(viewMatrix, viewMatrixLocation);
		uploadMatrices(modelMatrix, modelMatrixLocation);
	}

	@Override
	public void unbind() {
		GL20.glUseProgram(0);
	}

}
