package net.wohlfart.basic.states;

// wrapper for state objects
/**
 * <p>Enum class for all valid game states.</p>
 */
public enum GameStateEnum implements GameState {
    // @formatter:off
    NULL(new NullState()),
    SIMPLE(new SimpleState()),
    LIGHTING(new LightingState()),
    ;
    // @formatter:on

    private GameState delegate;

    GameStateEnum(GameState state) {
        this.delegate = state;
    }

    @Override
    public void setup() {
        delegate.setup();
    }

    @Override
    public void update(float tpf) {
        delegate.update(tpf);
    }

    @Override
    public void render() {
        delegate.render();
    }

    @Override
    public boolean isDone() {
        return delegate.isDone();
    }

    @Override
    public void destroy() {
        delegate.destroy();
    }

}
