package net.wohlfart.basic.states;

/**
 * A simple state that does nothing.
 */
class NullState implements GameState { // REVIEWED

    /** {@inheritDoc} */
    @Override
    public void setup() {
    }

    /** {@inheritDoc} */
    @Override
    public void update(final float tpf) {
    }

    /** {@inheritDoc} */
    @Override
    public void render() {
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDone() {
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void destroy() {
    }

}
