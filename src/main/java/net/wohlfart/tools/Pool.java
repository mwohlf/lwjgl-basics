package net.wohlfart.tools;

// see: http://libgdx.badlogicgames.com/nightlies/docs/api/com/badlogic/gdx/utils/Pool.html
/**
 * <p>Abstract Pool class.</p>
 *
 *
 *
 */
abstract public class Pool<T> {
    public final int max;

    private final Array<T> freeObjects;

    /**
     * Creates a pool with an initial capacity of 16 and no maximum.
     */
    public Pool() {
        this(16, Integer.MAX_VALUE);
    }

    /**
     * Creates a pool with the specified initial capacity and no maximum.
     *
     * @param initialCapacity a int.
     */
    public Pool(int initialCapacity) {
        this(initialCapacity, Integer.MAX_VALUE);
    }

    /**
     * <p>Constructor for Pool.</p>
     *
     * @param max
     *            The maximum number of free objects to store in this pool.
     * @param initialCapacity a int.
     */
    public Pool(int initialCapacity, int max) {
        freeObjects = new Array<T>(false, initialCapacity);
        this.max = max;
    }

    /**
     * <p>newObject.</p>
     *
     * @return a T object.
     */
    abstract protected T newObject();

    /**
     * Returns an object from this pool. The object may be new (from {@link #newObject()}) or reused (previously {@link #free(Object) freed}).
     *
     * @return a T object.
     */
    public T obtain() {
        return freeObjects.size == 0 ? newObject() : freeObjects.pop();
    }

    /**
     * Puts the specified object in the pool, making it eligible to be returned by {@link #obtain()}. If the pool already contains {@link #max} free objects,
     * the specified object is reset but not added to the pool.
     *
     * @param object a T object.
     */
    public void free(T object) {
        if (object == null) {
            throw new IllegalArgumentException("object cannot be null.");
        }
        if (freeObjects.size < max) {
            freeObjects.add(object);
        }
        if (object instanceof IPoolable) {
            ((IPoolable) object).reset();
        }
    }

    /**
     * Puts the specified objects in the pool. Null objects within the array are silently ignored.
     *
     * @see #free(Object)
     * @param objects a {@link net.wohlfart.tools.Array} object.
     */
    public void freeAll(Array<T> objects) {
        if (objects == null) {
            throw new IllegalArgumentException("object cannot be null.");
        }
        for (int i = 0; i < objects.size; i++) {
            final T object = objects.get(i);
            if (object == null) {
                continue;
            }
            if (freeObjects.size < max) {
                freeObjects.add(object);
            }
            if (object instanceof IPoolable) {
                ((IPoolable) object).reset();
            }
        }
    }

    /**
     * Removes all free objects from this pool.
     */
    public void clear() {
        freeObjects.clear();
    }

    /** Objects implementing this interface will have {@link #reset()} called when passed to {@link #free(Object)}. */
    static public interface IPoolable {
        /** Resets the object for reuse. Object references should be nulled and fields may be set to default values. */
        public void reset();
    }
}
