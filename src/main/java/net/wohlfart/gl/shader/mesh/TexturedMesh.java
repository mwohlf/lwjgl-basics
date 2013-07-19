package net.wohlfart.gl.shader.mesh;

import java.util.Arrays;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.Vertex;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TexturedMesh class.
 */
public class TexturedMesh implements IsRenderable {

    private final int vaoHandle;
    private final int texHandle;
    private final int indicesCount;
    private final int indexOffset;
    private final int indexElemSize;
    private final int indicesType;


    private TexturedMesh(int vaoHandle, int indicesType, int indexElemSize, int indicesCount, int indexOffset, int texHandle) {
        this.vaoHandle = vaoHandle;
        this.texHandle = texHandle;
        this.indicesType = indicesType;
        this.indexElemSize = indexElemSize;
        this.indicesCount = indicesCount;
        this.indexOffset = indexOffset;
    }

    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texHandle);
        GL11.glDrawElements(indicesType, indicesCount, indexElemSize, indexOffset);
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }




    public static class Builder extends AbstractMeshBuilder {
        protected static final Logger LOGGER = LoggerFactory.getLogger(Builder.class);

        private float size = 1f;                              // length of one side of the texture

        public void setSize(float size) {
            this.size = size;
        }

        @Override
        public IsRenderable build() {
            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);

            int texHandle = resolveTexHandle();

            createVboHandle(createStream());

            byte[] indices = new byte[] { 0, 1, 2, 2, 3, 0 };
            createIdxBufferHandle(indices);

            final int[] offset = {0};
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                    + ShaderAttributeHandle.COLOR.getByteCount()
                    + ShaderAttributeHandle.TEXTURE_COORD.getByteCount()
                    ;
            ShaderAttributeHandle.POSITION.enable(stride, offset);
            ShaderAttributeHandle.COLOR.enable(stride, offset);
            ShaderAttributeHandle.TEXTURE_COORD.enable(stride, offset);
            ShaderAttributeHandle.NORMAL.disable();

            // done with the VAO
            GL30.glBindVertexArray(0);

            return new TexturedMesh(vaoHandle, GL11.GL_TRIANGLES, GL11.GL_UNSIGNED_BYTE, indices.length, 0, texHandle);
        }

        protected float[] createStream() {

            final Vector3f[] vectors = new Vector3f[] { // @formatter:off
                    new Vector3f(-(size/2f), +(size/2f), 0),
                    new Vector3f(-(size/2f), -(size/2f), 0),
                    new Vector3f(+(size/2f), -(size/2f), 0),
                    new Vector3f(+(size/2f), +(size/2f), 0),
            };

            applyRotationAndTranslation(Arrays.asList(vectors));

            final Vertex[] vertices = new Vertex[] {
                    new Vertex() {{
                        setXYZ(vectors[0].x, vectors[0].y, vectors[0].z);
                        setRGB(1, 0, 0);
                        setST(0, 0);
                    }},
                    new Vertex() {{
                        setXYZ(vectors[1].x, vectors[1].y, vectors[1].z);
                        setRGB(0, 1, 0);
                        setST(0, 1);
                    }},
                    new Vertex() {{
                        setXYZ(vectors[2].x, vectors[2].y, vectors[2].z);
                        setRGB(0, 0, 1);
                        setST(1, 1);
                    }},
                    new Vertex() {{
                        setXYZ(vectors[3].x, vectors[3].y, vectors[3].z);
                        setRGB(1, 1, 1);
                        setST(1, 0);
                    }}
            }; // @formatter:on


            // Put each 'Vertex' in one FloatBuffer the order depends on the shaders positions!
            float[] stream = new float[vertices.length * (3 + 4 + 2)];
            int i = 0;
            for (Vertex vertex : vertices) {
                stream[i++] = vertex.getXYZ()[0];
                stream[i++] = vertex.getXYZ()[1];
                stream[i++] = vertex.getXYZ()[2];
                stream[i++] = vertex.getRGBA()[0];
                stream[i++] = vertex.getRGBA()[1];
                stream[i++] = vertex.getRGBA()[2];
                stream[i++] = vertex.getRGBA()[3];
                stream[i++] = vertex.getST()[0];
                stream[i++] = vertex.getST()[1];
            }
            return stream;
        }

    }

}
