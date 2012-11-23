package net.wohlfart.gl;

public enum NullState implements IState {
	INSTANCE;

	@Override
	public void setup() {
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
	public void teardown() {
	}

}
