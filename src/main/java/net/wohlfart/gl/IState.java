package net.wohlfart.gl;

import net.wohlfart.basic.IGameContext;

public interface IState {

	public void setup(IGameContext gameContext);
	
	public void update(float tpf);
	public void render();
	public boolean isDone();
	
	public void teardown(IGameContext gameContext);
	
}
