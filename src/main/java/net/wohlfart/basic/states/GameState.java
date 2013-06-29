package net.wohlfart.basic.states;

/**
 * A GameState is a set of elements that can be rendered, it also contains some business logic that might be
 * triggered during the update method call.
 */
public interface GameState { // REVIEWED

    /**
     * The initial setup of the state, must be called before this object can be used.
     */
    void setup();

    /**
     * A periodic update of this state.
     *
     * @param tpf
     *            a float providing the time since the last update call
     */
    void update(float tpf);

    /**
     * call To render in the current graphic context.
     */
    void render();

    /**
     * Check if this state is terminated by the user or some other event.
     *
     * @return a boolean value of true when this state is done and needs to be switched out.
     */
    boolean isDone();

    /**
     * Destroy instance and free all resources.
     */
    void destroy();

}
