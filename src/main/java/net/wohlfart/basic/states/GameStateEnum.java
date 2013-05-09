package net.wohlfart.basic.states;

import net.wohlfart.basic.IGameContext;

// wrapper for state objects
/**
 * <p>Enum class for all valid game states.</p>
 */
public enum GameStateEnum implements GameState {
    // @formatter:off
    NULL,
    SIMPLE,
    LIGHTING,
    ;
    // @formatter:on

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
