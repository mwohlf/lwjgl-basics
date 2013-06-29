package net.wohlfart.basic;

/**
 * An interface defining how to provide access to random classes from the application context.
 */
public interface IGameContext {  // REVIEWED

    /**
     * This method returns a bean of the provided class from the application context.
     *
     * @param clazz
     *            a {@link java.lang.Class} object defining the type of class to be returned from the context.
     * @param <T>
     *            a class object defining the type we want from the context
     *
     * @return a T object from the application context
     */
    <T> T getBeanOfType(Class<T> clazz);

    /**
     * This method returns a bean of the provided class and with the provided name from the application context.
     *
     * @param clazz
     *            a {@link java.lang.Class} object defining the type of class to be returned from the context.
     * @param <T>
     *            a class object defining the type we want from the context
     * @param name
     *            the name or id of the element we want from the context
     *
     * @return a T object from the application context
     */
    <T> T getBeanOfName(Class<T> clazz, String name);

}
