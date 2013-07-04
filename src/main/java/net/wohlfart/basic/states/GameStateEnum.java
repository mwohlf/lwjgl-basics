package net.wohlfart.basic.states;

import net.wohlfart.basic.IGameContext;

// @formatter:off
/**
 * Enum class for all valid game states, its a wrapper for state objects,
 * the delegates must be picked up from the context at startup.
 * The key for pickup is GAME_STATE_PREFIX + "." + this.name()
 */
public enum GameStateEnum implements GameState { // REVIEWED
    NULL,
    SIMPLE,
    TESTING,
    CELESTIAL,
    ; // @formatter:on

    // for context lookup
    private static final String GAME_STATE_PREFIX = "GameState";

    private GameState delegate;

    public void inject(IGameContext context) {
        delegate = context.getBeanOfName(GameState.class, GAME_STATE_PREFIX + "." + this.name());
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
