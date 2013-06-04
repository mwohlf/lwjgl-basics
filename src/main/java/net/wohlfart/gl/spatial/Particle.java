package net.wohlfart.gl.spatial;

import net.wohlfart.tools.ObjectPool;
import net.wohlfart.tools.ObjectPool.PoolableObject;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Vector3f;

public class Particle implements PoolableObject {
    private static final int MAX = 10000;

    private float lifetime;
    private Vector3f normal;
    private Vector3f position;          // m
    private Vector3f speed;             // m/s
    private Vector3f acceleration;      // m/s^2


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
        /*
        Vector3f v = new Vector3f(acceleration);
        v.scale(delta);
        SimpleMath.add(speed, v, speed);
        Vector3f w = new Vector3f(speed);
        w.scale(delta);
        */
        Vector3f w = new Vector3f(speed);
        w.scale(0.001f);
        SimpleMath.add(position, w, position);

    }


    @Override
    public void reset() {
        pool.returnObject(this);
    }


    public Vector3f getPosition() {
        return position;
    }



}
