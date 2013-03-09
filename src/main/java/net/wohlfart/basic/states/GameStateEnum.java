package net.wohlfart.basic.states;

// wrapper for state objects
/**
 * <p>Enum class for all valid game states.</p>
 */
public enum GameStateEnum {
    // @formatter:off
    NULL(new NullState()),
    SIMPLE(new SimpleState());
    // @formatter:on

    private GameState state;

    GameStateEnum(GameState state) {
        this.state = state;
    }

    /**
     * <p>getValue.</p>
     *
     * @return a {@link net.wohlfart.basic.states.GameState} object.
     */
    public GameState getValue() {
        return state;
    }

}
