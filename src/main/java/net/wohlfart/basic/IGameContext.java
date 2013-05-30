package net.wohlfart.basic;

/**
 * <p>
 * An interface for classes that provide access to random classes from the application context by providing the type of the class.
 * </p>
 */
public interface IGameContext {

    /**
     * <p>
     * This method returns a bean of the provided class from the application context.
     * </p>
     * 
     * @param clazz
     *            a {@link java.lang.Class} object defining the type of class to be returned from the context.
     * @param <T>
     *            a class object defining the type we want from the context
     * @return a T object from the application context
     */
    <T> T getBeanOfType(Class<T> clazz);

    /**
     * <p>
     * This method returns a bean of the provided class and with the provided name from the application context.
     * </p>
     */
    <V> V getBeanOfName(Class<V> clazz, String name);

}
