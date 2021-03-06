package net.wohlfart.gl.spatial;

import java.util.ArrayList;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.Vertex;
import net.wohlfart.gl.shader.mesh.AbstractMeshBuilder;
import net.wohlfart.gl.texture.CelestialTexture;
import net.wohlfart.gl.texture.CelestialType;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;

public class CelestialBodyMesh implements IsRenderable {

    private final int vaoHandle;
    private final int texHandle;

    private final int count;
    private final int indexElemSize;
    private final int trianglePrimitive;

    CelestialBodyMesh(int vaoHandle, int textureHandle, int trianglePrimitive, int count, int indexElemSize) {
        this.vaoHandle = vaoHandle;
        this.texHandle = textureHandle;
        this.trianglePrimitive = trianglePrimitive;
        this.count = count;
        this.indexElemSize = indexElemSize;
    }

    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texHandle);
        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        // Setup what to do when the texture has to be scaled
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
        if (indexElemSize == -1) {
            GL11.glDrawArrays(trianglePrimitive, 0, count);
        } else {
            GL11.glDrawElements(trianglePrimitive, count, indexElemSize, 0);
        }
        GL30.glBindVertexArray(0);
    }

    @Override
    public void destroy() {
        GL30.glBindVertexArray(0);
        GL30.glDeleteVertexArrays(vaoHandle);
    }



    public static class Builder extends AbstractMeshBuilder {

        private int lod = 0;  // starts at 0 for min lod
        private CelestialType celestialType = CelestialType.CONTINENTAL_PLANET;

        private float radius = 1f;
        private long seed = 0;

        public void setLod(int lod) {
            this.lod = lod;
        }

        public void setCelestialType(CelestialType celestialType) {
            this.celestialType = celestialType;
        }

        public void setRadius(float radius) {
            this.radius = radius;
        }

        public void setSeed(long seed) {
            this.seed = seed;
        }



        @Override
        public IsRenderable build() {
            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle);

            CelestialTexture texture = new CelestialTexture(512, 512, celestialType, seed, GL13.GL_TEXTURE0);
            texture.init();
            int textureHandle = texture.getTextureId();

            final ArrayList<Vertex> vertices = new ArrayList<Vertex>();
            createVboHandle(createStream(vertices)); // this also binds the GL15.GL_ARRAY_BUFFER

            final int[] offset = {0};
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                             + ShaderAttributeHandle.NORMAL.getByteCount()
                             + ShaderAttributeHandle.TEXTURE_COORD.getByteCount()
                             ;
            ShaderAttributeHandle.POSITION.enable(stride, offset);
            ShaderAttributeHandle.NORMAL.enable(stride, offset);
            ShaderAttributeHandle.TEXTURE_COORD.enable(stride, offset);
            ShaderAttributeHandle.COLOR.disable();

            // we are done with the VAO state
            GL30.glBindVertexArray(0);

            return new CelestialBodyMesh(vaoHandle, textureHandle, GL11.GL_TRIANGLES, vertices.size(), -1);
        }


        float[] createStream(ArrayList<Vertex> vertices) {
            float radFragment = SimpleMath.HALF_PI / (lod + 1);

            for (int slice = 0; slice <= lod; slice++) { // slices along the y-axis

                for (int sector = 0; sector <= lod; sector++) {  // sectors rotate around the y-axis

                    float a1 = SimpleMath.HALF_PI + (radFragment * sector);
                    float a2 = a1 + radFragment;
                    float b1 = SimpleMath.PI + (radFragment * sector);
                    float b2 = b1 + radFragment;
                    float c1 = SimpleMath.THREE_HALF_PI + (radFragment * sector);
                    float c2 = c1 + radFragment;
                    float d1 = SimpleMath.TWO_PI + (radFragment * sector);
                    float d2 = d1 + radFragment;

                    float up1 = (radFragment * slice);
                    float up2 = up1 + radFragment;
                    float down1 = -(radFragment * slice);
                    float down2 = down1 - radFragment;

                    vertices.addAll(createQuad(a1, up1, a2, up2));
                    vertices.addAll(createQuad(b1, up1, b2, up2));
                    vertices.addAll(createQuad(c1, up1, c2, up2));
                    vertices.addAll(createQuad(d1, up1, d2, up2));

                    vertices.addAll(createQuad(a1, down2, a2, down1));
                    vertices.addAll(createQuad(b1, down2, b2, down1));
                    vertices.addAll(createQuad(c1, down2, c2, down1));
                    vertices.addAll(createQuad(d1, down2, d2, down1));

                }

            }

            // we need to create this:
            float[] stream;

            int elemSize = 3 + 3 + 2;
            stream = new float[vertices.size() * elemSize];
            for (int i = 0; i < (vertices.size()); i++) {
                final Vertex vertex = vertices.get(i);
                // coords:
                stream[i* elemSize + 0] = vertex.getXYZ()[0];
                stream[i* elemSize + 1] = vertex.getXYZ()[1];
                stream[i* elemSize + 2] = vertex.getXYZ()[2];
                // normal:
                stream[i* elemSize + 3] = vertex.getNormal()[0];
                stream[i* elemSize + 4] = vertex.getNormal()[1];
                stream[i* elemSize + 5] = vertex.getNormal()[2];
                // texture:
                stream[i* elemSize + 6] = vertex.getST()[0];
                stream[i* elemSize + 7] = vertex.getST()[1];
            }

            return stream;

        }


        private ArrayList<Vertex> createQuad(final float u1, final float v1, final float u2, final float v2) {
            ArrayList<Vertex> result = new ArrayList<Vertex>();

            final Vector3f bottomLeft = createVector(u1, v1);
            final Vector3f bottomRight = createVector(u2, v1);
            final Vector3f topRight = createVector(u2, v2);
            final Vector3f topLeft = createVector(u1, v2);

            if (!tooClose(bottomLeft, bottomRight)) {
                result.add(new Vertex(){{
                    setXYZ(bottomLeft);
                    setNormal(bottomLeft);
                    setST(u1/SimpleMath.PI + 0.5f, v1/SimpleMath.PI + 0.5f);
                }});
                result.add(new Vertex(){{
                    setXYZ(bottomRight);
                    setNormal(bottomRight);
                    setST(u2/SimpleMath.PI + 0.5f, v1/SimpleMath.PI + 0.5f);
                }});
                result.add(new Vertex(){{
                    setXYZ(topRight);
                    setNormal(topRight);
                    setST(u2/SimpleMath.PI + 0.5f, v2/SimpleMath.PI + 0.5f);
                }});
            }

            if (!tooClose(topRight, topLeft)) {
                result.add(new Vertex(){{
                    setXYZ(topRight);
                    setNormal(topRight);
                    setST(u2/SimpleMath.PI + 0.5f, v2/SimpleMath.PI + 0.5f);
                }});
                result.add(new Vertex(){{
                    setXYZ(topLeft);
                    setNormal(topLeft);
                    setST(u1/SimpleMath.PI + 0.5f, v2/SimpleMath.PI + 0.5f);
                }});
                result.add(new Vertex(){{
                    setXYZ(bottomLeft);
                    setNormal(bottomLeft);
                    setST(u1/SimpleMath.PI + 0.5f, v1/SimpleMath.PI + 0.5f);
                }});
            }

            return result;
        }

        /**
         *
         * @param lat rad in y direction [-PI ... +PI]
         * @param lon rad around y axix [0 ... 2PI]
         * @return
         */
        private Vector3f createVector(float lon, float lat) {
            final float xx = (float) Math.cos(lat) * (float) Math.sin(lon); // 0,0 -> 0;
            final float yy = (float) Math.sin(lat); // -PI -> -1; 0 -> 0 ; PI -> +1
            final float zz = (float) Math.cos(lat) * (float) Math.cos(lon); // 0,0 -> 1
            Vector3f vector = new Vector3f(xx, yy, zz);
            vector.scale(radius);
            return vector;
        }

        private static final float EPSILON = 0.01f;
        private boolean tooClose(Vector3f vec1, Vector3f vec2) {
            return (Math.abs(vec1.x - vec2.x) < EPSILON)
                    && (Math.abs(vec1.y - vec2.y) < EPSILON)
                    && (Math.abs(vec1.z - vec2.z) < EPSILON);
        }

    }

}
