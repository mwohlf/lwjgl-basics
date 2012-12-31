package net.wohlfart.basic.states;

import net.wohlfart.basic.Game;

public interface GameState {

	public abstract void setup(final Game game);

	public abstract void update(final float tpf);

	public abstract void render();

	public abstract boolean isDone();

	public abstract void dispose(final Game game);

}
