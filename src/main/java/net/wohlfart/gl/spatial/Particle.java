package net.wohlfart.gl.spatial;

import java.nio.FloatBuffer;

import net.wohlfart.tools.ObjectPool;
import net.wohlfart.tools.ObjectPool.PoolableObject;
import net.wohlfart.tools.SimpleMath;
import net.wohlfart.tools.Vertex;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;

public class Particle implements PoolableObject {
    private static final int MAX = 10000;

    private float lifetime;
    private Vector3f position;          // m
    private Vector3f speed;             // m/s
    private Vector3f acceleration;      // m/s^2

    private FloatBuffer verticesBuffer;

    private static ObjectPool<Particle> pool = new ObjectPool<Particle>(10) {
        @Override
        protected Particle newObject() {
            return new Particle();
        }
    };

    public static Particle create(float lifetime,
            Vector3f position, Vector3f speed, Vector3f acceleration) {
        final Particle result = pool.borrowObject();
        result.lifetime = lifetime;
        result.position = position;
        result.speed = speed;
        result.acceleration = acceleration;
        return result;
    }


    public void update(float delta) {

        Vector3f w = new Vector3f(speed);
        w.scale(0.1f);
        SimpleMath.add(position, w, position);


        final Vector3f[] vectors = new Vector3f[] { // @formatter:off
                new Vector3f(-0.5f, +0.5f, 0),
                new Vector3f(-0.5f, -0.5f, 0),
                new Vector3f(+0.5f, -0.5f, 0),
                new Vector3f(+0.5f, +0.5f, 0) };  // @formatter:on

        if (position != null) {
            for (final Vector3f vec : vectors) {
                SimpleMath.add(position, vec, vec);
            }
        }


        final Vertex[] vertices = new Vertex[] {   // @formatter:on
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
            setXYZ(vectors[2].x, vectors[2].y, vectors[2].z);
            setRGB(0, 0, 1);
            setST(1, 1);
        }},
        new Vertex() {{
            setXYZ(vectors[3].x, vectors[3].y, vectors[3].z);
            setRGB(1, 1, 1);
            setST(1, 0);
        }},
        new Vertex() {{
            setXYZ(vectors[0].x, vectors[0].y, vectors[0].z);
            setRGB(1, 0, 0);
            setST(0, 0);
        }}
        }; // @formatter:on


        verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
        for (int i = 0; i < vertices.length; i++) {
           verticesBuffer.put(vertices[i].getXYZW());
           verticesBuffer.put(vertices[i].getRGBA());
           verticesBuffer.put(vertices[i].getST());
        }

    }

    public FloatBuffer getVerticesBuffer() {
        verticesBuffer.rewind();
        return verticesBuffer;
    }

    @Override
    public void reset() {
        pool.returnObject(this);
    }

}
