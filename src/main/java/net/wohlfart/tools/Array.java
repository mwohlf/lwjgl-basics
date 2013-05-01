package net.wohlfart.tools;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A resizable, ordered or unordered array of objects.
 * If unordered, this class avoids a memory copy when removing elements
 * (the last element is moved to the removed element's position).
 *
 * @author Nathan Sweet
 */
class Array<T> implements Iterable<T> {
    /**
     * Provides direct access to the underlying array.
     * If the Array's generic type is not Object, this field may only be accessed if the
     * {@link Array#Array(boolean, int, Class)} constructor was used.
     */
    public T[] items;

    public int size;
    public boolean ordered;

    private ArrayIterator<T> iterator;

    /**
     * Creates an ordered array with a capacity of 16.
     */
    public Array() {
        this(true, 16);
    }

    /**
     * Creates an ordered array with the specified capacity.
     *
     * @param capacity a int.
     */
    public Array(int capacity) {
        this(true, capacity);
    }

    /**
     * <p>Constructor for Array.</p>
     *
     * @param ordered
     *            If false, methods that remove elements may change the order of other elements in the array, which avoids a memory copy.
     * @param capacity
     *            Any elements added beyond this will cause the backing array to be grown.
     */
    public Array(boolean ordered, int capacity) {
        this.ordered = ordered;
        items = (T[]) new Object[capacity];
    }

    /**
     * Creates a new array with {@link #items} of the specified type.
     *
     * @param ordered
     *            If false, methods that remove elements may change the order of other elements in the array, which avoids a memory copy.
     * @param capacity
     *            Any elements added beyond this will cause the backing array to be grown.
     * @param arrayType a {@link java.lang.Class} object.
     */
    public Array(boolean ordered, int capacity, Class<T> arrayType) {
        this.ordered = ordered;
        items = (T[]) java.lang.reflect.Array.newInstance(arrayType, capacity);
    }

    /**
     * Creates an ordered array with {@link #items} of the specified type and a capacity of 16.
     *
     * @param arrayType a {@link java.lang.Class} object.
     */
    public Array(Class<T> arrayType) {
        this(true, 16, arrayType);
    }

    /**
     * Creates a new array containing the elements in the specified array. The new array will have the same type of backing array and will be ordered if the
     * specified array is ordered. The capacity is set to the number of elements, so any subsequent elements added will cause the backing array to be grown.
     *
     * @param array a {@link net.wohlfart.tools.Array} object.
     */
    public Array(Array<T> array) {
        this(array.ordered, array.size, (Class<T>) array.items.getClass().getComponentType());
        size = array.size;
        System.arraycopy(array.items, 0, items, 0, size);
    }

    /**
     * Creates a new ordered array containing the elements in the specified array. The new array will have the same type of backing array. The capacity is set
     * to the number of elements, so any subsequent elements added will cause the backing array to be grown.
     *
     * @param array an array of T objects.
     */
    public Array(T[] array) {
        this(true, array);
    }

    /**
     * Creates a new array containing the elements in the specified array. The new array will have the same
     * type of backing array. The capacity is set to the
     * number of elements, so any subsequent elements added will cause the backing array to be grown.
     *
     * @param ordered
     *            If false, methods that remove elements may change the order of other elements in the array, which avoids a memory copy.
     * @param array an array of T objects.
     */
    public Array(boolean ordered, T[] array) {
        this(ordered, array.length, (Class<T>) array.getClass().getComponentType());
        size = array.length;
        System.arraycopy(array, 0, items, 0, size);
    }

    /**
     * <p>add.</p>
     *
     * @param value a T object.
     */
    public void add(T value) {
        T[] items = this.items;
        if (size == items.length) {
            items = resize(Math.max(8, (int) (size * 1.75f)));
        }
        items[size++] = value;
    }

    /**
     * <p>addAll.</p>
     *
     * @param array a {@link net.wohlfart.tools.Array} object.
     */
    public void addAll(Array<T> array) {
        addAll(array, 0, array.size);
    }

    /**
     * <p>addAll.</p>
     *
     * @param array a {@link net.wohlfart.tools.Array} object.
     * @param offset a int.
     * @param length a int.
     */
    public void addAll(Array<T> array, int offset, int length) {
        if (offset + length > array.size) {
            throw new IllegalArgumentException("offset + length must be <= size: " + offset + " + " + length + " <= " + array.size);
        }
        addAll(array.items, offset, length);
    }

    /**
     * <p>addAll.</p>
     *
     * @param array an array of T objects.
     */
    public void addAll(T[] array) {
        addAll(array, 0, array.length);
    }

    /**
     * <p>addAll.</p>
     *
     * @param array an array of T objects.
     * @param offset a int.
     * @param length a int.
     */
    public void addAll(T[] array, int offset, int length) {
        T[] items = this.items;
        final int sizeNeeded = size + length;
        if (sizeNeeded > items.length) {
            items = resize(Math.max(8, (int) (sizeNeeded * 1.75f)));
        }
        System.arraycopy(array, offset, items, size, length);
        size += length;
    }

    /**
     * <p>get.</p>
     *
     * @param index a int.
     * @return a T object.
     */
    public T get(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        return items[index];
    }

    /**
     * <p>set.</p>
     *
     * @param index a int.
     * @param value a T object.
     */
    public void set(int index, T value) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        items[index] = value;
    }

    /**
     * <p>insert.</p>
     *
     * @param index a int.
     * @param value a T object.
     */
    public void insert(int index, T value) {
        T[] items = this.items;
        if (size == items.length) {
            items = resize(Math.max(8, (int) (size * 1.75f)));
        }
        if (ordered) {
            System.arraycopy(items, index, items, index + 1, size - index);
        } else {
            items[size] = items[index];
        }
        size++;
        items[index] = value;
    }

    /**
     * <p>swap.</p>
     *
     * @param first a int.
     * @param second a int.
     */
    public void swap(int first, int second) {
        if (first >= size) {
            throw new IndexOutOfBoundsException(String.valueOf(first));
        }
        if (second >= size) {
            throw new IndexOutOfBoundsException(String.valueOf(second));
        }
        final T[] items = this.items;
        final T firstValue = items[first];
        items[first] = items[second];
        items[second] = firstValue;
    }

    /**
     * <p>contains.</p>
     *
     * @param identity
     *            If true, == comparison will be used. If false, .equals() comaparison will be used.
     * @param value a T object.
     * @return a boolean.
     */
    public boolean contains(T value, boolean identity) {
        final T[] items = this.items;
        int i = size - 1;
        if (identity || value == null) {
            while (i >= 0) {
                if (items[i--] == value) {
                    return true;
                }
            }
        } else {
            while (i >= 0) {
                if (value.equals(items[i--])) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * <p>indexOf.</p>
     *
     * @param value a T object.
     * @param identity a boolean.
     * @return a int.
     */
    public int indexOf(T value, boolean identity) {
        final T[] items = this.items;
        if (identity || value == null) {
            for (int i = 0, n = size; i < n; i++) {
                if (items[i] == value) {
                    return i;
                }
            }
        } else {
            for (int i = 0, n = size; i < n; i++) {
                if (value.equals(items[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * <p>lastIndexOf.</p>
     *
     * @param value a T object.
     * @param identity a boolean.
     * @return a int.
     */
    public int lastIndexOf(T value, boolean identity) {
        final T[] items = this.items;
        if (identity || value == null) {
            for (int i = size - 1; i >= 0; i--) {
                if (items[i] == value) {
                    return i;
                }
            }
        } else {
            for (int i = size - 1; i >= 0; i--) {
                if (value.equals(items[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * <p>removeValue.</p>
     *
     * @param value a T object.
     * @param identity a boolean.
     * @return a boolean.
     */
    public boolean removeValue(T value, boolean identity) {
        final T[] items = this.items;
        if (identity || value == null) {
            for (int i = 0, n = size; i < n; i++) {
                if (items[i] == value) {
                    removeIndex(i);
                    return true;
                }
            }
        } else {
            for (int i = 0, n = size; i < n; i++) {
                if (value.equals(items[i])) {
                    removeIndex(i);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Removes and returns the item at the specified index.
     *
     * @param index a int.
     * @return a T object.
     */
    public T removeIndex(int index) {
        if (index >= size) {
            throw new IndexOutOfBoundsException(String.valueOf(index));
        }
        final T[] items = this.items;
        final T value = items[index];
        size--;
        if (ordered) {
            System.arraycopy(items, index + 1, items, index, size - index);
        } else {
            items[index] = items[size];
        }
        items[size] = null;
        return value;
    }

    /**
     * Removes and returns the last item.
     *
     * @return a T object.
     */
    public T pop() {
        --size;
        final T item = items[size];
        items[size] = null;
        return item;
    }

    /**
     * Returns the last item.
     *
     * @return a T object.
     */
    public T peek() {
        return items[size - 1];
    }

    /**
     * Returns the first item.
     *
     * @return a T object.
     */
    public T first() {
        return items[0];
    }

    /**
     * <p>clear.</p>
     */
    public void clear() {
        final T[] items = this.items;
        for (int i = 0, n = size; i < n; i++) {
            items[i] = null;
        }
        size = 0;
    }

    /**
     * Reduces the size of the backing array to the size of the actual items. This is useful to release memory when many items have been removed, or if it is
     * known that more items will not be added.
     */
    public void shrink() {
        resize(size);
    }

    /**
     * Increases the size of the backing array to acommodate the specified number of additional items. Useful before adding many items to avoid multiple backing
     * array resizes.
     *
     * @return {@link #items}
     * @param additionalCapacity a int.
     */
    public T[] ensureCapacity(int additionalCapacity) {
        final int sizeNeeded = size + additionalCapacity;
        if (sizeNeeded >= items.length) {
            resize(Math.max(8, sizeNeeded));
        }
        return items;
    }

    /**
     * Creates a new backing array with the specified size containing the current items.
     *
     * @param newSize a int.
     * @return an array of T objects.
     */
    protected T[] resize(int newSize) {
        final T[] items = this.items;
        final T[] newItems = (T[]) java.lang.reflect.Array.newInstance(items.getClass().getComponentType(), newSize);
        System.arraycopy(items, 0, newItems, 0, Math.min(size, newItems.length));
        this.items = newItems;
        return newItems;
    }

    /**
     * Sorts this array. The array elements must implement {@link java.lang.Comparable}. This method is not thread safe (uses {@link net.wohlfart.tools.Sort#instance()}).
     */
    public void sort() {
        Sort.instance().sort(items, 0, size);
    }

    /**
     * Sorts the array. This method is not thread safe (uses {@link net.wohlfart.tools.Sort#instance()}).
     *
     * @param comparator a {@link java.util.Comparator} object.
     */
    public void sort(Comparator<T> comparator) {
        Sort.instance().sort(items, comparator, 0, size);
    }

    /**
     * <p>reverse.</p>
     */
    public void reverse() {
        for (int i = 0, lastIndex = size - 1, n = size / 2; i < n; i++) {
            final int ii = lastIndex - i;
            final T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    /**
     * <p>shuffle.</p>
     */
    public void shuffle() {
        for (int i = size - 1; i >= 0; i--) {
            final int ii = SimpleMath.random(i);
            final T temp = items[i];
            items[i] = items[ii];
            items[ii] = temp;
        }
    }

    /**
     * {@inheritDoc}
     *
     * Returns an iterator for the items in the array. Remove is supported. Note that the same iterator instance is returned each time this method is called.
     * Use the {@link ArrayIterator} constructor for nested or multithreaded iteration.
     */
    @Override
    public Iterator<T> iterator() {
        if (iterator == null) {
            iterator = new ArrayIterator<T>(this);
        } else {
            iterator.index = 0;
        }
        return iterator;
    }

    /**
     * Reduces the size of the array to the specified size. If the array is already smaller than the specified size, no action is taken.
     *
     * @param newSize a int.
     */
    public void truncate(int newSize) {
        if (size <= newSize) {
            return;
        }
        for (int i = newSize; i < size; i++) {
            items[i] = null;
        }
        size = newSize;
    }

    /**
     * Returns a random item from the array, or null if the array is empty.
     *
     * @return a T object.
     */
    public T random() {
        if (size == 0) {
            return null;
        }
        return items[SimpleMath.random(0, size - 1)];
    }

    /**
     * <p>toArray.</p>
     *
     * @return an array of T objects.
     */
    public T[] toArray() {
        return (T[]) toArray(items.getClass().getComponentType());
    }

    /**
     * <p>toArray.</p>
     *
     * @param type a {@link java.lang.Class} object.
     * @param <V> a V object.
     * @return an array of V objects.
     */
    public <V> V[] toArray(Class<V> type) {
        final V[] result = (V[]) java.lang.reflect.Array.newInstance(type, size);
        System.arraycopy(items, 0, result, 0, size);
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof Array)) {
            return false;
        }
        final Array<T> array = (Array<T>) object;
        final int n = size;
        if (n != array.size) {
            return false;
        }
        final Object[] items1 = this.items;
        final Object[] items2 = array.items;
        for (int i = 0; i < n; i++) {
            final Object o1 = items1[i];
            final Object o2 = items2[i];
            if (!(o1 == null ? o2 == null : o1.equals(o2))) {
                return false;
            }
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(items);
        result = prime * result + (ordered ? 1231 : 1237);
        result = prime * result + size;
        return result;
    }


    /** {@inheritDoc} */
    @Override
    public String toString() {
        if (size == 0) {
            return "[]";
        }
        final T[] items = this.items;
        final StringBuilder buffer = new StringBuilder(32);
        buffer.append('[');
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(", ");
            buffer.append(items[i]);
        }
        buffer.append(']');
        return buffer.toString();
    }

    /**
     * <p>toString.</p>
     *
     * @param separator a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String toString(String separator) {
        if (size == 0) {
            return "";
        }
        final T[] items = this.items;
        final StringBuilder buffer = new StringBuilder(32);
        buffer.append(items[0]);
        for (int i = 1; i < size; i++) {
            buffer.append(separator);
            buffer.append(items[i]);
        }
        return buffer.toString();
    }

    static public class ArrayIterator<T> implements Iterator<T> {
        private final Array<T> array;
        int index;

        public ArrayIterator(Array<T> array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return index < array.size;
        }

        @Override
        public T next() {
            if (index >= array.size) {
                throw new NoSuchElementException(String.valueOf(index));
            }
            return array.items[index++];
        }

        @Override
        public void remove() {
            index--;
            array.removeIndex(index);
        }

        public void reset() {
            index = 0;
        }
    }

    static public class ArrayIterable<T> implements Iterable<T> {
        private final ArrayIterator<T> iterator;

        public ArrayIterable(Array<T> array) {
            iterator = new ArrayIterator<T>(array);
        }

        @Override
        public Iterator<T> iterator() {
            iterator.reset();
            return iterator;
        }
    }
}
