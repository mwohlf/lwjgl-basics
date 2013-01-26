package net.wohlfart.gl.renderer;

import java.nio.FloatBuffer;

import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.ShaderUniformHandle;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix4f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultRenderer implements Renderer {
	protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultRenderer.class);


	public DefaultRenderer() {
	}


	@Override
	public void setup() {
	}

	@Override
	public int getVertexAttrib(ShaderAttributeHandle handle) {
		return handle.getLocation();
		//return attributeMap.get(handle);
	}

	@Override
	public void set(ShaderUniformHandle handle, Matrix4f matrix) {
		FloatBuffer matrix44Buffer = BufferUtils.createFloatBuffer(16);
		matrix.store(matrix44Buffer);
		matrix44Buffer.flip();
		//GL20.glUniformMatrix4(matrixMap.get(handle), false, matrix44Buffer);
		//handle.setValue(matrix44Buffer);
		GL20.glUniformMatrix4(handle.getLocation(), false, matrix44Buffer);

	}

	@Override
	public void set(ShaderUniformHandle handle, ReadableColor readableColor) {
		FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4);
		colorBuffer.put(new float[] {
				readableColor.getRed()/255f,
				readableColor.getGreen()/255f,
				readableColor.getBlue()/255f,
				readableColor.getAlpha()/255f,
		});
		colorBuffer.flip();
		//GL20.glUniform4(matrixMap.get(handle), colorBuffer);
		//handle.setValue(colorBuffer);
		GL20.glUniform4(handle.getLocation(), colorBuffer);

	}


}
