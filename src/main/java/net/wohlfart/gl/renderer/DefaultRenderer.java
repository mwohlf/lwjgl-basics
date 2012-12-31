package net.wohlfart.gl.renderer;

import java.nio.FloatBuffer;
import java.util.HashMap;

import net.wohlfart.gl.shader.AttributeHandle;
import net.wohlfart.gl.shader.DefaultShaderProgram;
import net.wohlfart.gl.shader.IShaderProgram;
import net.wohlfart.gl.shader.ShaderProgram;
import net.wohlfart.gl.shader.UniformHandle;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRenderer implements Renderer {
	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultRenderer.class);


	private final ShaderProgram shaderProgram;
	private final HashMap<AttributeHandle, Integer> attributeMap = new HashMap<>(AttributeHandle.values().length);
	private final HashMap<UniformHandle, Integer> matrixMap = new HashMap<>(UniformHandle.values().length);


	public DefaultRenderer() {
		this(new DefaultShaderProgram());
	}

	public DefaultRenderer(final ShaderProgram shaderProgram) {
		this.shaderProgram = shaderProgram;
		shaderProgram.setup();
		shaderProgram.bind();
	}

	@Override
	public IShaderProgram getShaderProgram() {
		return shaderProgram;
	}

	@Override
	public void setup() {

		for (AttributeHandle attributeHandle : AttributeHandle.values()) {
			int location = attributeHandle.getLocation(shaderProgram);
			if (location < 0) {
				LOGGER.warn("location for AttributeHandle '{}' is <0, value was '{}'",
						new Object[] {attributeHandle, location});
			}
			attributeMap.put(attributeHandle, location);
		}
		for (UniformHandle matrixHandle : UniformHandle.values()) {
			int location = matrixHandle.getLocation(shaderProgram);
			if (location < 0) {
				LOGGER.warn("location for MatrixHandle '{}' is <0, value was '{}'",
						new Object[] {matrixHandle, location});
			}
			matrixMap.put(matrixHandle, location);
		}
	}

	@Override
	public int getVertexAttrib(AttributeHandle handle) {
		return attributeMap.get(handle);
	}

	@Override
	public void set(UniformHandle handle, Matrix4f matrix) {
		FloatBuffer matrix44Buffer = BufferUtils.createFloatBuffer(16);
		matrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		GL20.glUniformMatrix4(matrixMap.get(handle), false, matrix44Buffer);
	}

	@Override
	public void set(UniformHandle handle, ReadableColor readableColor) {
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4);
		colorBuffer.put(new float[] {
				readableColor.getRed()/255f,
				readableColor.getGreen()/255f,
				readableColor.getBlue()/255f,
				readableColor.getAlpha()/255f,
		});
		colorBuffer.flip();
		GL20.glUniform4(matrixMap.get(handle), colorBuffer);
	}

	@Override
	public void dispose() {
		shaderProgram.unbind();
		shaderProgram.dispose();
	}

}
