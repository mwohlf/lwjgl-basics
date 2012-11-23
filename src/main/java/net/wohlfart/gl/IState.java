package net.wohlfart.gl;

public interface IState {

	public void setup();
	
	public void update(float tpf);
	public void render();
	public boolean isDone();
	
	public void teardown();
	
}
