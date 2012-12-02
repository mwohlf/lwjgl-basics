package net.wohlfart.gl;

import net.wohlfart.basic.IGameContext;

public enum NullState implements IState {
	INSTANCE;

	@Override
	public void setup(IGameContext gameContext) {
	}

	@Override
	public void update(float tpf) {
	}

	@Override
	public void render() {
	}

	@Override
	public boolean isDone() {
		return true;
	}

	@Override
	public void teardown(IGameContext gameContext) {
	}

}
