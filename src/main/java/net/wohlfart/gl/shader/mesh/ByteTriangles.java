package net.wohlfart.gl.shader.mesh;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class ByteTriangles implements Indices<Byte> {

    private final List<Byte> content;

    public ByteTriangles(final Byte[] content) {
        this.content = Arrays.asList(content);
    }

    public ByteTriangles(List<Byte> content) {
        this.content = content;
    }

    @Override
    public List<Byte> getContent() {
        return content;
    }

    @Override
    public int getStructure() {
        return GL11.GL_TRIANGLE_STRIP;
    }

    @Override
    public int getElemSize() {
        // TODO Auto-generated method stub
        return 0;
    }

}
