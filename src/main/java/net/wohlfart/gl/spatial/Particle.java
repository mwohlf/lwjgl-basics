package net.wohlfart.gl.spatial;

import net.wohlfart.gl.shader.GraphicContextHolder;
import net.wohlfart.gl.shader.Vertex;
import net.wohlfart.gl.view.Camera;
import net.wohlfart.tools.ObjectPool;
import net.wohlfart.tools.ObjectPool.PoolableObject;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public class Particle implements PoolableObject {

    private static final ObjectPool<Particle> pool = new ObjectPool<Particle>(500) {
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
                setNormal(0,0,-1);
            }},
            new Vertex() {{
                setXYZ(vectors[1].x, vectors[1].y, vectors[1].z);
                setRGB(0, 1, 0);
                setST(0, 1);
                setNormal(0,0,-1);
            }},
            new Vertex() {{
                setXYZ(vectors[2].x, vectors[2].y, vectors[2].z);
                setRGB(0, 0, 1);
                setST(1, 1);
                setNormal(0,0,-1);
            }},

            new Vertex() {{
                setXYZ(vectors[2].x, vectors[2].y, vectors[2].z);
                setRGB(0, 0, 1);
                setST(1, 1);
                setNormal(0,0,-1);
            }},
            new Vertex() {{
                setXYZ(vectors[3].x, vectors[3].y, vectors[3].z);
                setRGB(1, 1, 1);
                setST(1, 0);
                setNormal(0,0,-1);
            }},
            new Vertex() {{
                setXYZ(vectors[0].x, vectors[0].y, vectors[0].z);
                setRGB(1, 0, 0);
                setST(0, 0);
                setNormal(0,0,-1);
            }}
    };

    private float[] verticesBuffer = new float[vertices.length * (3 + 4 + 3 + 2)];
    // @formatter:on

    public static Particle create(float lifetime,
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


    public void update(float delta, Vector3f emitterPos) {

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

        verticesBuffer = setupStream(position, emitterPos);
    }

    public float[] getVerticesBuffer() {
        return verticesBuffer;
    }

    @Override
    public void reset() {
        pool.returnObject(this);
    }

    public boolean isAlive() {
        return !(lifetime < 0);
    }

    private float[] setupStream(Vector3f position, Vector3f emitterPos) {
        Camera cam = GraphicContextHolder.CONTEXT_HOLDER.getCamera();

        Vector3f worldPos = new Vector3f(position);
        Vector3f.add(worldPos, emitterPos, worldPos);

        Vector3f normal = new Vector3f(cam.getPosition());
        Vector3f.sub(normal, worldPos, normal);
        normal.normalise();

        // facing cam
        Quaternion q = new Quaternion();
        SimpleMath.createQuaternion(new Vector3f(0,0,-1), normal, q);

        vectors[0].set(-0.5f, +0.5f, 0);
        vectors[1].set(-0.5f, -0.5f, 0);
        vectors[2].set(+0.5f, -0.5f, 0);
        vectors[3].set(+0.5f, +0.5f, 0);

        for (final Vector3f vec : vectors) {
            SimpleMath.mul(q, vec, vec);
            //SimpleMath.multLocal(vec, q);
        }

        for (final Vector3f vec : vectors) {
            SimpleMath.add(position, vec, vec);
        }

        vertices[0].setXYZ(vectors[0].x, vectors[0].y, vectors[0].z);
        vertices[1].setXYZ(vectors[1].x, vectors[1].y, vectors[1].z);
        vertices[2].setXYZ(vectors[2].x, vectors[2].y, vectors[2].z);
        vertices[3].setXYZ(vectors[2].x, vectors[2].y, vectors[2].z);
        vertices[4].setXYZ(vectors[3].x, vectors[3].y, vectors[3].z);
        vertices[5].setXYZ(vectors[0].x, vectors[0].y, vectors[0].z);

        vertices[0].setNormal(normal);
        vertices[1].setNormal(normal);
        vertices[2].setNormal(normal);
        vertices[3].setNormal(normal);
        vertices[4].setNormal(normal);
        vertices[5].setNormal(normal);

        int i = 0;
        for (Vertex vertex : vertices) {
            verticesBuffer[i++] = vertex.getXYZ()[0];
            verticesBuffer[i++] = vertex.getXYZ()[1];
            verticesBuffer[i++] = vertex.getXYZ()[2];

            verticesBuffer[i++] = vertex.getRGBA()[0];
            verticesBuffer[i++] = vertex.getRGBA()[1];
            verticesBuffer[i++] = vertex.getRGBA()[2];
            verticesBuffer[i++] = vertex.getRGBA()[3];

            verticesBuffer[i++] = vertex.getNormal()[0];
            verticesBuffer[i++] = vertex.getNormal()[1];
            verticesBuffer[i++] = vertex.getNormal()[2];

            verticesBuffer[i++] = vertex.getST()[0];
            verticesBuffer[i++] = vertex.getST()[1];
        }
        return verticesBuffer;
    }



}
