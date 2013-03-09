package net.wohlfart.basic.states;

/**
 * <p>GameState interface.</p>
 *
 *
 *
 */
public interface GameState {

    /**
     * <p>setup.</p>
     */
    public abstract void setup();

    /**
     * <p>update.</p>
     *
     * @param tpf a float.
     */
    public abstract void update(float tpf);

    /**
     * <p>render.</p>
     */
    public abstract void render();

    /**
     * <p>isDone.</p>
     *
     * @return a boolean.
     */
    public abstract boolean isDone();

    /**
     * <p>dispose.</p>
     */
    public abstract void dispose();

}
