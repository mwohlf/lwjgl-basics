package net.wohlfart.gl.input;

/**
 * <p>
 * InputDispatcher interface.
 * </p>
 * 
 * 
 * 
 */
public interface InputDispatcher {

    /**
     * <p>
     * register.
     * </p>
     * 
     * @param listener
     *            a {@link java.lang.Object} object.
     */
    void register(Object listener);

    /**
     * <p>
     * unregister.
     * </p>
     * 
     * @param listener
     *            a {@link java.lang.Object} object.
     */
    void unregister(Object listener);

    /**
     * <p>
     * post.
     * </p>
     * 
     * @param event
     *            a {@link java.lang.Object} object.
     */
    void post(Object event);

    /**
     * <p>
     * destroy.
     * </p>
     */
    void destroy();

}
