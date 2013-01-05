package net.wohlfart.gl.shader.mesh;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ByteLines implements Indices<Byte> {

	private final List<Byte> content;

	public ByteLines(final Byte[] content) {
		this.content = Arrays.asList(content);
	}

	public ByteLines(List<Byte> content) {
		this.content = content;
	}

	@Override
	public List<Byte> getContent() {
		return content;
	}

	@Override
	public int getStructure() {
		return GL11.GL_LINES;
	}

	@Override
	public int getElemSize() {
		return GL11.GL_UNSIGNED_BYTE;
	}

}
