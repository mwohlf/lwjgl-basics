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

    private static ObjectPool<Particle> pool = new ObjectPool<Particle>(10) {
        @Override
        protected Particle newObject() {
            return new Particle();
        }
    };

    private final Vector3f a = new Vector3f();
    private final Vector3f v = new Vector3f();


    private float lifetime;        // [s]
    private Vector3f position;     // [m]
    private Vector3f velocity;     // [m/s]
    private Vector3f acceleration; // [m/s^2]

    private final FloatBufferStrategy floatBufferStrategy = new FloatBufferStrategy();
    private FloatBuffer verticesBuffer;


    public static Particle create(  float lifetime,
                                    Vector3f position,
                                    Vector3f velocity,
                                    Vector3f acceleration) {
        final Particle result = pool.borrowObject();
        result.lifetime = lifetime;
        result.position = position;
        result.velocity = velocity;
        result.acceleration = acceleration;
        return result;
    }


    public void update(float delta) {

        lifetime -= delta;
        if (lifetime < 0) {
            return;
        }

        a.set(acceleration);
        a.scale(delta);
        velocity = Vector3f.add(velocity, a, velocity);

        v.set(velocity);
        v.scale(delta);
        position = Vector3f.add(position, v, position);

        verticesBuffer = floatBufferStrategy.create(position);
    }

    public FloatBuffer getVerticesBuffer() {
        verticesBuffer.rewind();
        return verticesBuffer;
    }

    @Override
    public void reset() {
        pool.returnObject(this);
    }

    public boolean isAlive() {
        return !(lifetime < 0);
    }


    private static class FloatBufferStrategy {


        FloatBuffer create(Vector3f position) {

            vectors[0].set(-0.5f, +0.5f, 0);
            vectors[1].set(-0.5f, -0.5f, 0);
            vectors[2].set(+0.5f, -0.5f, 0);
            vectors[3].set(+0.5f, +0.5f, 0);

            if (position != null) {
                for (final Vector3f vec : vectors) {
                    SimpleMath.add(position, vec, vec);
                }
            }

            vertices[0].setXYZ(vectors[0].x, vectors[0].y, vectors[0].z);
            vertices[1].setXYZ(vectors[1].x, vectors[1].y, vectors[1].z);
            vertices[2].setXYZ(vectors[2].x, vectors[2].y, vectors[2].z);
            vertices[3].setXYZ(vectors[2].x, vectors[2].y, vectors[2].z);
            vertices[4].setXYZ(vectors[3].x, vectors[3].y, vectors[3].z);
            vertices[5].setXYZ(vectors[0].x, vectors[0].y, vectors[0].z);

            FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length * Vertex.elementCount);
            for (int i = 0; i < vertices.length; i++) {
               verticesBuffer.put(vertices[i].getXYZW());
               verticesBuffer.put(vertices[i].getRGBA());
               verticesBuffer.put(vertices[i].getST());
            }
            return verticesBuffer;
        }

        // @formatter:off
        final Vector3f[] vectors = new Vector3f[] {
                new Vector3f(-0.5f, +0.5f, 0),
                new Vector3f(-0.5f, -0.5f, 0),
                new Vector3f(+0.5f, -0.5f, 0),
                new Vector3f(+0.5f, +0.5f, 0) };

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
                };
        // @formatter:on

    }

}
