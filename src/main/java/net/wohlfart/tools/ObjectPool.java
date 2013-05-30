package net.wohlfart.tools;

import java.util.ArrayList;

import net.wohlfart.tools.ObjectPool.PoolableObject;

public abstract class ObjectPool<T extends PoolableObject> {

    static public interface PoolableObject {
        public void reset();
    }

    public final int capacity;
    private final ArrayList<T> freeObjects;

    public ObjectPool(int capacity) {
        freeObjects = new ArrayList<T>();
        this.capacity = capacity;
    }

    abstract protected T newObject();

    public T borrowObject() {
        return freeObjects.size() == 0 ? newObject() : freeObjects.get(freeObjects.size() - 1);
    }

    public void returnObject(T object) {
        object.reset();
        freeObjects.add(object);
    }

}
