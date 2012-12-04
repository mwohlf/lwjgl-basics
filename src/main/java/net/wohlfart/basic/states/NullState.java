package net.wohlfart.basic.states;

import net.wohlfart.basic.Game;

import org.lwjgl.util.vector.Matrix4f;

public class NullState implements IGameState {

	NullState() {
		
	}
	
	
	@Override
	public void setup(Game game, Matrix4f projectionMatrix) {
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
	public void teardown(Game gameContext) {
	}

}
