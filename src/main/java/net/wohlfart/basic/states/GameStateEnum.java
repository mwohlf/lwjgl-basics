package net.wohlfart.basic.states;


// wrapper for state objects
public enum GameStateEnum {
	NULL(new NullState()),
	SIMPLE(new SimpleState()),
	CELESTIAL(new CelestialState());
	
	private GameState state;
	
	GameStateEnum(GameState state) {
		this.state = state;
	}
	
	public GameState getValue() {
		return state;
	}

}
