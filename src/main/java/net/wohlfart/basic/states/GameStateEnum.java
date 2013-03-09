package net.wohlfart.basic.states;

// wrapper for state objects
/**
 * <p>GameStateEnum class.</p>
 *
 *
 *
 */
public enum GameStateEnum {
    // @formatter:off
    NULL(new NullState()),
    SIMPLE(new SimpleState()),
    CELESTIAL(new CelestialState());
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
