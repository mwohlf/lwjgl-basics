package net.wohlfart.basic;

/**
 * <p>IGameContext interface.</p>
 */
public interface IGameContext {

    /**
     * <p>getBeanOfType.</p>
     *
     * @param clazz a {@link java.lang.Class} object.
     * @param <T> a T object.
     * @return a T object.
     */
    <T> T getBeanOfType(Class<T> clazz);

}
