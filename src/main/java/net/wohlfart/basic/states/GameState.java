package net.wohlfart.basic.states;

public interface GameState {

    public abstract void setup();

    public abstract void update(float tpf);

    public abstract void render();

    public abstract boolean isDone();

    public abstract void dispose();

}
