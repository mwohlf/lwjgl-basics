package net.wohlfart.basic;

/**
 * <p>interface for classes that provide access to random classes from the application context.</p>
 */
public interface IGameContext {

    /**
     * <p>returns a bean of the provided class from the application context.</p>
     *
     * @param clazz a {@link java.lang.Class} object.
     * @param <T> a T object.
     * @return a T object from the application context
     */
    <T> T getBeanOfType(Class<T> clazz);

}
