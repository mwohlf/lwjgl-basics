package net.wohlfart.gl.elements.hud.widgets;



/**
 * <p>Statistics class.</p>
 *
 *
 *
 */
public class Statistics extends FormattedLabel {

    private static final float RENDER_INTERVAL = 1f;
    private float delta = 0;
    private int count = 0;

    /**
     * <p>Constructor for Statistics.</p>
     *
     * @param x a int.
     * @param y a int.
     */
    public Statistics(int x, int y) {
        super(x, y, "fps:{0,number,#0.0} tpf:{1,number,#0.000}");
    }

    /**
     * <p>update.</p>
     *
     * @param tpf a float.
     */
    public void update(float tpf) {
        delta += tpf;
        count += 1;
        if (delta > RENDER_INTERVAL) {
            setValue(new Object[]{count/delta, delta/count});
            delta = count = 0;
        }
    }

}
