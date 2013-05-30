package net.wohlfart.basic.states;

/**
 * A simple state that does nothing.
 */
final class NullState implements GameState {

    @Override
    public void setup() {
    }

    @Override
    public void update(final float tpf) {
    }

    @Override
    public void render() {
    }

    @Override
    public boolean isDone() {
        return true;
    }

    @Override
    public void destroy() {
    }

}
