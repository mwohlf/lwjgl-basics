package net.wohlfart.widgets;

abstract class AbstractWidget {

    public abstract void paint(Renderer renderer);

    public void paintChildren(Renderer renderer) {

    }

}
