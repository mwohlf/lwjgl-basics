package net.wohlfart.tools;

import java.util.ArrayList;

import net.wohlfart.tools.ObjectPool.PoolableObject;

public abstract class ObjectPool<T extends PoolableObject> {

    static public interface PoolableObject {
        public void reset();
    }

    private final int capacity;
    private final ArrayList<T> freeObjects;
    private final int created = 0;

    public ObjectPool(int capacity) {
        freeObjects = new ArrayList<T>();
        this.capacity = capacity;
    }

    abstract protected T newObject();

    public T borrowObject() {
        if (freeObjects.size() > 0) {
            return freeObjects.get(freeObjects.size() - 1);
        } else {
            return newObject();
        }
    }

    public void returnObject(T object) {
        freeObjects.add(object);
    }



}
