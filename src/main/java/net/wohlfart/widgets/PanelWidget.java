package net.wohlfart.widgets;

public class PanelWidget extends AbstractWidget {

    protected int x = 0;
    protected int y = 0;
    protected int w = 0;
    protected int h = 0;

    public PanelWidget(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    @Override
    public void paint(final Renderer renderer) {
        renderer.fillRect(x, y, w, h);
    }

}
