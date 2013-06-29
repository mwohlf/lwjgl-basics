package net.wohlfart.gl.spatial;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.gl.shader.Vertex;
import net.wohlfart.gl.shader.mesh.AbstractMeshBuilder;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public class CelestialBodyMesh implements IsRenderable {

    private final int vaoHandle;
    private final int textureHandle;

    private final int count;
    private final int indexElemSize;
    private final int trianglePrimitive;

    CelestialBodyMesh(int vaoHandle, int textureHandle, int trianglePrimitive, int count, int indexElemSize) {
        this.vaoHandle = vaoHandle;
        this.textureHandle = textureHandle;
        this.trianglePrimitive = trianglePrimitive;
        this.count = count;
        this.indexElemSize = indexElemSize;
    }

    @Override
    public void render() {
        GL30.glBindVertexArray(vaoHandle);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureHandle);
        // Setup the ST coordinate system
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL14.GL_MIRRORED_REPEAT);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL14.GL_MIRRORED_REPEAT);
        // Setup what to do when the texture has to be scaled
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
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



    public static class IcosahedronBuilder extends AbstractMeshBuilder {

        // the start data (LOD: 0)
        float t = (1.0f + SimpleMath.sqrt(5.0f)) / 2.0f;
        private final List<Vector3f> initialVertices = new ArrayList<Vector3f>(Arrays.<Vector3f> asList(new Vector3f[] {// @formatter:off
                new Vector3f(-1f, t, 0),
                new Vector3f(+1f, t, 0),
                new Vector3f(-1f,-t, 0),
                new Vector3f(+1f,-t, 0),

                new Vector3f( 0f,-1, t),
                new Vector3f( 0f, 1, t),
                new Vector3f( 0f,-1,-t),
                new Vector3f( 0f, 1,-t),

                new Vector3f( t,  0,-1),
                new Vector3f( t,  0, 1),
                new Vector3f(-t,  0,-1),
                new Vector3f(-t,  0, 1), }));



        private final List<Integer> initialIndices = Arrays.asList(new Integer[] {

                 // 5 faces around point 0
                 0, 11,  5,
                 0,  5,  1,
                 0,  1,  7,
                 0,  7, 10,
                 0, 10, 11,

                 // 5 adjacent faces
                 1,  5,  9,
                 5, 11,  4,
                11, 10,  2,
                10,  7,  6,
                 7,  1,  8,

                 // 5 faces around point 3
                 3,  9,  4,
                 3,  4,  2,
                 3,  2,  6,
                 3,  6,  8,
                 3,  8,  9,

                 // 5 adjacent faces
                 4,  9,  5,
                 2,  4, 11,
                 6,  2, 10,
                 8,  6,  7,
                 9,  8,  1,
        }); // @formatter:on

        private float radius;

        public void setRadius(float radius) {
            this.radius = radius;
        }

        @Override
        public IsRenderable build() {
            // rotate one vertex to the top

            for (final Vector3f vec : initialVertices) {
                final float l = vec.length();
                vec.scale(radius / l);
            }
            Quaternion q = new Quaternion();
            q = SimpleMath.createQuaternion(initialVertices.get(4), new Vector3f(0,1,0), q);

            for (final Vector3f vec : initialVertices) {
                vec.set(SimpleMath.multLocal(vec, q));
            }


            // we need to create this:
            float[] stream;
            int[] indices;

            int elemSize = 3 + 3 + 2;
            stream = new float[initialVertices.size() * elemSize];
            for (int i = 0; i < (initialVertices.size()); i++) {
                // coords:
                Vector3f point = new Vector3f(initialVertices.get(i));
                stream[i* elemSize + 0] = point.getX();
                stream[i* elemSize + 1] = point.getY();
                stream[i* elemSize + 2] = point.getZ();
                // normal:
                point.normalise();
                stream[i* elemSize + 3] = point.getX();
                stream[i* elemSize + 4] = point.getY();
                stream[i* elemSize + 5] = point.getZ();
                // texture:
                Vector2f loc = SimpleMath.getPositionVector(initialVertices.get(i));
                stream[i* elemSize + 6] = loc.x;
                stream[i* elemSize + 7] = loc.y;
            }


            indices = new int[initialIndices.size()];
            for (int i = 0; i < initialIndices.size(); i++) {
                indices[i] = initialIndices.get(i);
            }


            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle); // @formatter:off

            final int textureHandle = createTextureHandle("/gfx/images/ash_uvgrid07.png", GL13.GL_TEXTURE0);  // also binds the texture
            createVboHandle(stream); // this also binds the GL15.GL_ARRAY_BUFFER
            createIdxBufferHandle(indices); // this also binds the GL15.GL_ELEMENT_ARRAY_BUFFER

            int offset;
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                             + ShaderAttributeHandle.NORMAL.getByteCount()
                             + ShaderAttributeHandle.TEXTURE_COORD.getByteCount();

            offset = 0;
            ShaderAttributeHandle.POSITION.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(),
                                       ShaderAttributeHandle.POSITION.getFloatCount(), GL11.GL_FLOAT,
                                       false, stride, offset);

            offset += ShaderAttributeHandle.POSITION.getByteCount();
            ShaderAttributeHandle.NORMAL.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.NORMAL.getLocation(),
                                       ShaderAttributeHandle.NORMAL.getFloatCount(), GL11.GL_FLOAT,
                                       false, stride, offset);

            offset += ShaderAttributeHandle.NORMAL.getByteCount();
            ShaderAttributeHandle.TEXTURE_COORD.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.TEXTURE_COORD.getLocation(),
                                       ShaderAttributeHandle.TEXTURE_COORD.getFloatCount(), GL11.GL_FLOAT,
                                       false, stride, offset);

            ShaderAttributeHandle.COLOR.disable();

            // we are done with the VAO state
            GL30.glBindVertexArray(0); // @formatter:on

            final int indexElemSize = GL11.GL_UNSIGNED_INT;
            final int indicesCount = indices.length;
            final int primitiveType = GL11.GL_TRIANGLES;

            return new CelestialBodyMesh(vaoHandle, textureHandle, primitiveType, indicesCount, indexElemSize);
        }

    }


    public static class RevolvedSphereBuilder extends AbstractMeshBuilder {

        private int lod = 0;  // starts at 0 for min lod

        public void setLod(int lod) {
            this.lod = lod;
        }

        @Override
        public IsRenderable build() {
            float radFragment = SimpleMath.HALF_PI / (lod + 1);
            ArrayList<Vertex> vertices = new ArrayList<Vertex>();

            for (int slice = 0; slice <= lod; slice++) { // slices are along the y-axis

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
                Vertex vertex = vertices.get(i);
                // coords:
                stream[i* elemSize + 0] = vertex.getXYZW()[0];
                stream[i* elemSize + 1] = vertex.getXYZW()[1];
                stream[i* elemSize + 2] = vertex.getXYZW()[2];
                // normal:
                stream[i* elemSize + 3] = vertex.getNormal()[0];
                stream[i* elemSize + 4] = vertex.getNormal()[1];
                stream[i* elemSize + 5] = vertex.getNormal()[2];
                // texture:
                stream[i* elemSize + 6] = vertex.getST()[0];
                stream[i* elemSize + 7] = vertex.getST()[1];
            }



            final int vaoHandle = GL30.glGenVertexArrays();
            GL30.glBindVertexArray(vaoHandle); // @formatter:off

            final int textureHandle = createTextureHandle("/gfx/images/ash_uvgrid07.png", GL13.GL_TEXTURE0);  // also binds the texture
            createVboHandle(stream); // this also binds the GL15.GL_ARRAY_BUFFER

            int offset;
            final int stride = ShaderAttributeHandle.POSITION.getByteCount()
                             + ShaderAttributeHandle.NORMAL.getByteCount()
                             + ShaderAttributeHandle.TEXTURE_COORD.getByteCount();

            offset = 0;
            ShaderAttributeHandle.POSITION.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.POSITION.getLocation(),
                                       ShaderAttributeHandle.POSITION.getFloatCount(), GL11.GL_FLOAT,
                                       false, stride, offset);

            offset += ShaderAttributeHandle.POSITION.getByteCount();
            ShaderAttributeHandle.NORMAL.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.NORMAL.getLocation(),
                                       ShaderAttributeHandle.NORMAL.getFloatCount(), GL11.GL_FLOAT,
                                       false, stride, offset);

            offset += ShaderAttributeHandle.NORMAL.getByteCount();
            ShaderAttributeHandle.TEXTURE_COORD.enable();
            GL20.glVertexAttribPointer(ShaderAttributeHandle.TEXTURE_COORD.getLocation(),
                                       ShaderAttributeHandle.TEXTURE_COORD.getFloatCount(), GL11.GL_FLOAT,
                                       false, stride, offset);

            ShaderAttributeHandle.COLOR.disable();

            // we are done with the VAO state
            GL30.glBindVertexArray(0); // @formatter:on

            final int primitiveType = GL11.GL_TRIANGLES;
            final int count = vertices.size();

            return new CelestialBodyMesh(vaoHandle, textureHandle, primitiveType, count, -1);

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
                    setST(u1/SimpleMath.PI, v1/SimpleMath.PI);
                }});
                result.add(new Vertex(){{
                    setXYZ(bottomRight);
                    setNormal(bottomRight);
                    setST(u2/SimpleMath.PI, v1/SimpleMath.PI);
                }});
                result.add(new Vertex(){{
                    setXYZ(topRight);
                    setNormal(topRight);
                    setST(u2/SimpleMath.PI, v2/SimpleMath.PI);
                }});
            }

            if (!tooClose(topRight, topLeft)) {
                result.add(new Vertex(){{
                    setXYZ(topRight);
                    setNormal(topRight);
                    setST(u2/SimpleMath.PI, v2/SimpleMath.PI);
                }});
                result.add(new Vertex(){{
                    setXYZ(topLeft);
                    setNormal(topLeft);
                    setST(u1/SimpleMath.PI, v2/SimpleMath.PI);
                }});
                result.add(new Vertex(){{
                    setXYZ(bottomLeft);
                    setNormal(bottomLeft);
                    setST(u1/SimpleMath.PI, v1/SimpleMath.PI);
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
            return new Vector3f(xx, yy, zz);
        }

        private static final float EPSILON = 0.01f;
        private boolean tooClose(Vector3f vec1, Vector3f vec2) {
            return (Math.abs(vec1.x - vec2.x) < EPSILON)
                    && (Math.abs(vec1.y - vec2.y) < EPSILON)
                    && (Math.abs(vec1.z - vec2.z) < EPSILON);
        }

    }

}
