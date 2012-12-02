package net.wohlfart.basic;

import net.wohlfart.gl.IState;
import net.wohlfart.model.CelestialState;

// wrapper for state objects
public enum GameStates {
	SIMPLE(new SimpleState()),
	CELESTIAL(new CelestialState());
	
	private IState state;
	
	GameStates(IState state) {
		this.state = state;
	}
	
	public IState getValue() {
		return state;
	}

}
