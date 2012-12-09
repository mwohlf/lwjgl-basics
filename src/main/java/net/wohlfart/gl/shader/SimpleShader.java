package net.wohlfart.gl.shader;

import net.wohlfart.basic.Game;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SimpleShader extends AbstractShader{
	protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleShader.class);

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f modelMatrix;

	// variable names are used inside the vertex shader
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
		programId = linkAndValidate(vertexShaderId, fragmentShaderId);

		// get attribute location
		inPosition = GL20.glGetAttribLocation(programId, POSITION);
		inColor = GL20.glGetAttribLocation(programId, COLOR);
		inTexCoord = GL20.glGetAttribLocation(programId, TEXTURE_COORD);

		// get matrices uniform locations
		projectionMatrixLocation = GL20.glGetUniformLocation(programId, PROJECTION_MATRIX);
		viewMatrixLocation = GL20.glGetUniformLocation(programId, VIEW_MATRIX);
		modelMatrixLocation = GL20.glGetUniformLocation(programId, MODEL_MATRIX);
	}


	@Override
	public void destroy() {
		unlink(programId, vertexShaderId, fragmentShaderId);
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


	protected Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}

	public void setProjectionMatrix(final Matrix4f projectionMatrix) {
		this.projectionMatrix = projectionMatrix;
	}


	protected Matrix4f getViewMatrix() {
		return viewMatrix;
	}

	public void setViewMatrix(final Matrix4f viewMatrix) {
		this.viewMatrix = viewMatrix;
	}


	protected Matrix4f getModelMatrix() {
		return modelMatrix;
	}

	public void setModelMatrix(final Matrix4f modelMatrix) {
		this.modelMatrix = modelMatrix;
	}


}
