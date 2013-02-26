package net.wohlfart.gl.elements.hud.widgets;



public class Statistics extends FormattedLabel {

    private float delta = 0;
    private int count = 0;
    private final float RENDER_INTERVAL = 1f;

    public Statistics(int x, int y) {
        super(x, y, "fps:{0,number,#0.0} tpf:{1,number,#0.000}");
    }

    public void update(float tpf) {
        delta += tpf;
        count += 1;
        if (delta > RENDER_INTERVAL) {
            setValue(new Object[]{count/delta, delta/count});
            delta = count = 0;
        }
    }

}
