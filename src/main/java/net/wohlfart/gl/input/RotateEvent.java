package net.wohlfart.gl.input;

import net.wohlfart.tools.ObjectPool;
import net.wohlfart.tools.ObjectPool.PoolableObject;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

@SuppressWarnings("serial")
public class RotateEvent extends Quaternion implements PoolableObject {

    private static final float ROTATION_SPEED = SimpleMath.TWO_PI; // one rotation per sec

    private static ObjectPool<RotateEvent> pool = new ObjectPool<RotateEvent>(10) {
        @Override
        protected RotateEvent newObject() {
            return new RotateEvent();
        }
    };

    @Override
    public void reset() {
        pool.returnObject(this);
    }

    static RotateEvent rotateLeft(float time) {
        return rotate(time, new Vector3f(0, 1, 0));
    }

    static RotateEvent rotateRight(float time) {
        return rotate(time, new Vector3f(0, -1, 0));
    }

    static RotateEvent rotateUp(float time) {
        return rotate(time, new Vector3f(1, 0, 0));
    }

    static RotateEvent rotateDown(float time) {
        return rotate(time, new Vector3f(-1, 0, 0));
    }

    static RotateEvent rotateClockwise(float time) {
        return rotate(time, new Vector3f(0, 0, 1));
    }

    static RotateEvent rotateCounterClockwise(float time) {
        return rotate(time, new Vector3f(0, 0, -1));
    }

    private static RotateEvent rotate(float time, Vector3f axis) {
        final RotateEvent result = pool.borrowObject();
        final Quaternion q = new Quaternion();
        SimpleMath.rotate(q, ROTATION_SPEED / 360f, axis);
        result.set(q);
        return result;
    }

}
