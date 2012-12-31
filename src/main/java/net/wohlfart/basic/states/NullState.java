package net.wohlfart.basic.states;

import net.wohlfart.basic.Game;

public class NullState implements GameState {

	NullState() {

	}


	@Override
	public void setup(final Game game) {
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
	public void dispose(final Game gameContext) {
	}

}
