package net.wohlfart.widgets;

/**
 * <p>PanelWidget class.</p>
 *
 *
 *
 */
public class PanelWidget extends AbstractWidget {

    protected int x = 0;
    protected int y = 0;
    protected int w = 0;
    protected int h = 0;

    /**
     * <p>Constructor for PanelWidget.</p>
     *
     * @param x a int.
     * @param y a int.
     * @param w a int.
     * @param h a int.
     */
    public PanelWidget(int x, int y, int w, int h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    /** {@inheritDoc} */
    @Override
    public void paint(final Renderer renderer) {
        renderer.fillRect(x, y, w, h);
    }

}
