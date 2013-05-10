package net.wohlfart.gl.input;

/**
 * <p>InputSource interface.</p>
 *
 *
 *
 */
public interface InputSource {

    /**
     * <p>set the event sink.</p>
     *
     * @param delta a float.
     */
    void setInputDispatcher(InputDispatcher inputDispatcher);


    /**
     * <p>createInputEvents.</p>
     *
     * @param delta a float.
     */
    void createInputEvents(float delta);

    /**
     * <p>destroy.</p>
     */
    void destroy();


}
