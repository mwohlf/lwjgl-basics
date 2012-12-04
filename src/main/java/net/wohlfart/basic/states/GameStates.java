package net.wohlfart.basic.states;


// wrapper for state objects
public enum GameStates {
	NULL(new NullState()),
	SIMPLE(new SimpleState()),
	CELESTIAL(new CelestialState());
	
	private IGameState state;
	
	GameStates(IGameState state) {
		this.state = state;
	}
	
	public IGameState getValue() {
		return state;
	}

}
