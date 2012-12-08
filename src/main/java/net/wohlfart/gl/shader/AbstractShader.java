package net.wohlfart.gl.shader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.FloatBuffer;
import java.util.Scanner;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;


/**
 * handle the basic shader stuff
 * - matrices
 * - error detection
 *
 */
public abstract class AbstractShader implements IShader {

	private Matrix4f projectionMatrix;
	private Matrix4f viewMatrix;
	private Matrix4f modelMatrix;
		
	protected int loadShader(final String filename, int shaderType) {
		int shader = 0;
		Scanner scanner = null;
		try (InputStream inputStream = ClassLoader.class.getResourceAsStream(filename);) {

			scanner = new Scanner(inputStream);
			scanner.useDelimiter("\\A");
			if (!scanner.hasNext()) {
				throw new ShaderException("empty shader file");
			}
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
			if (shader == 0) {
				throw new ShaderException("returned 0");
			}
			ARBShaderObjects.glShaderSourceARB(shader, scanner.next());
			ARBShaderObjects.glCompileShaderARB(shader);

			int compileStatus = ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB);
			if (compileStatus == GL11.GL_FALSE) {
				throw new ShaderException("Error creating shader, couldn't compile, reason: " + getLogInfo(shader));
			}
			return shader;
		} catch (FileNotFoundException ex) {
			throw new ShaderException("file not found: '" + filename + "'", ex);
		} catch (IOException ex) {
			throw new ShaderException("stream problems", ex);
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	protected void uploadMatrices(final Matrix4f matrix, final int location) {
		FloatBuffer matrix44Buffer = BufferUtils.createFloatBuffer(16);
		matrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		GL20.glUniformMatrix4(location, false, matrix44Buffer);
	}

	protected String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj,
				ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
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
