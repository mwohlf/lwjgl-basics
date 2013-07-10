package net.wohlfart.gl.input;

import net.wohlfart.tools.ObjectPool;
import net.wohlfart.tools.ObjectPool.PoolableObject;

import org.lwjgl.util.vector.Vector3f;

@SuppressWarnings("serial")
public class MoveEvent extends Vector3f implements PoolableObject {
    private static final float MOVE_SPEED = 100f; // 100 units per sec
    private static final float WHEEL_SENSITIVITY = 0.05f; //

    private static ObjectPool<MoveEvent> pool = new ObjectPool<MoveEvent>(10) {
        @Override
        protected MoveEvent newObject() {
            return new MoveEvent();
        }
    };

    @Override
    public void reset() {
        // ignored since data are overridden when borrowed
        pool.returnObject(this);
    }

    // ---- package private static factory methods

    static MoveEvent wheel(float time, int delta) {
        return move(0, 0, time * MOVE_SPEED * WHEEL_SENSITIVITY * delta);
    }

    static MoveEvent moveRight(float time) {
        return move(+time * MOVE_SPEED, 0, 0);
    }

    static MoveEvent moveLeft(float time) {
        return move(-time * MOVE_SPEED, 0, 0);
    }

    static MoveEvent moveDown(float time) {
        return move(0, -time * MOVE_SPEED, 0);
    }

    static MoveEvent moveUp(float time) {
        return move(0, +time * MOVE_SPEED, 0);
    }

    static MoveEvent moveForward(float time) {
        return move(0, 0, -time * MOVE_SPEED);
    }

    static MoveEvent moveBack(float time) {
        return move(0, 0, +time * MOVE_SPEED);
    }

    private static MoveEvent move(float x, float y, float z) {
        final MoveEvent result = pool.borrowObject();
        result.x = x;
        result.y = y;
        result.z = z;
        return result;
    }

}
