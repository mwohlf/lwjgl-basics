package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.lwjgl.util.vector.Matrix4f;


/**
 *
 * notes:
 * glEnableVertexAttribArray(0); enables the use of the vertex attribute at index 0 in the vertex shader.
 *
 *
 *
 *
 *
 *
 * @author michael
 *
 */
public class SimpleShader extends AbstractShader{
	// variable names hardcoded inside the vertex shader
	private static final String MODEL_MATRIX = "modelToWorldMatrix";
	private static final String VIEW_MATRIX = "worldToCameraMatrix";
	private static final String PROJECTION_MATRIX = "cameraToClipMatrix";

	private static final String POSITION = "in_Position";
	private static final String COLOR = "in_Color";
	private static final String TEXTURE_COORD = "in_TexCoord";

	private static final String VERTEX_SHADER_FILE = "/shaders/simple/vertex.glsl";
	private static final String FRAGMENT_SHADER_FILE = "/shaders/simple/fragment.glsl";

	private int programId;
	private int vertexShaderId;
	private int fragmentShaderId;

	private int inPosition = -1;
	private int inColor = -1;
	private int inTexCoord = -1;

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

		// get attribute location
		inPosition = GL20.glGetAttribLocation(programId, POSITION);
		inColor = GL20.glGetAttribLocation(programId, COLOR);
		inTexCoord = GL20.glGetAttribLocation(programId, TEXTURE_COORD);

		// get matrices uniform locations
		projectionMatrixLocation = GL20.glGetUniformLocation(programId, PROJECTION_MATRIX);
		viewMatrixLocation = GL20.glGetUniformLocation(programId, VIEW_MATRIX);
		modelMatrixLocation = GL20.glGetUniformLocation(programId, MODEL_MATRIX);

		GL20.glValidateProgram(programId);
	}



	@Override
	public void destroy() {


	}



	@Override
	public void bind() {
		GL20.glUseProgram(programId);

		uploadMatrices(getProjectionMatrix(), projectionMatrixLocation);
		uploadMatrices(getViewMatrix(), viewMatrixLocation);
		uploadMatrices(getModelMatrix(), modelMatrixLocation);
	}


	@Override
	public void unbind() {
		GL20.glUseProgram(0);
	}


	@Override
	public void setCurrentModelMatrix(final Matrix4f modelMatrix) {
		setModelMatrix(modelMatrix);
		uploadMatrices(getModelMatrix(), modelMatrixLocation);
	}

	@Override
	public int getInPosition() {
		return inPosition;
	}

	@Override
	public int getInColor() {
		return inColor;
	}

	@Override
	public int getInTexCoord() {
		return inTexCoord;
	}

}
