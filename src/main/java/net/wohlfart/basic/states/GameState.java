package net.wohlfart.basic.states;

/**
 * <p>
 * A GameState is a set of elements that can be rendered together with some business logic and
 * </p>
 * 
 * 
 * 
 */
public interface GameState {

    /**
     * <p>
     * The initial setup of the state.
     * </p>
     */
    void setup();

    /**
     * <p>
     * A periodic update of this state.
     * </p>
     * 
     * @param tpf
     *            a float the time since the last update
     */
    void update(float tpf);

    /**
     * <p>
     * Do render in the current graphic context.
     * </p>
     */
    void render();

    /**
     * <p>
     * Check if this state is terminated by the user or some other event.
     * </p>
     * 
     * @return a boolean value of true when this state is done and needs to be switched out.
     */
    boolean isDone();

    /**
     * <p>
     * Destroy instance and free all resources.
     * </p>
     */
    void destroy();

}
