package net.wohlfart.basic.states;

/**
 * a simple state that does nothing.
 */
final class NullState implements GameState { // REVIEWED

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
