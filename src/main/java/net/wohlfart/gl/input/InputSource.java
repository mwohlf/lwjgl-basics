package net.wohlfart.gl.input;

/**
 * <p>InputSource interface.</p>
 *
 *
 *
 */
public interface InputSource {

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
