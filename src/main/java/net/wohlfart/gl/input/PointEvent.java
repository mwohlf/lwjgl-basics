package net.wohlfart.gl.input;

import net.wohlfart.tools.ObjectPool;
import net.wohlfart.tools.ObjectPool.PoolableObject;

/*
 * the high level commands, base class for all kind of high level events/commands
 */
/**
 * <p>CommandEvent class.</p>
 *
 *
 *
 */
public class PointEvent implements PoolableObject  {

    private int x;
    private int y;
    private Key key;

    enum Key {
        PICK, CONTEXT;
    }

    private static ObjectPool<PointEvent> pool = new ObjectPool<PointEvent>(10) {
        @Override
        protected PointEvent newObject() {
            return new PointEvent();
        }
    };

    public static PointEvent create(int screenX, int screenY, Key key) {
        PointEvent result = pool.borrowObject();
        result.x = screenX;
        result.y = screenY;
        result.key = key;
        return result;
    }

    @Override
    public void reset() {
        pool.returnObject(this);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

}
