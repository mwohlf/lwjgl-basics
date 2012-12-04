package net.wohlfart.basic.states;

import net.wohlfart.basic.Game;

import org.lwjgl.util.vector.Matrix4f;

public interface IGameState {

	public void setup(Game game, Matrix4f projectionMatrix);

	public void update(float tpf);
	public void render();
	public boolean isDone();

	public void teardown(Game game);


}
