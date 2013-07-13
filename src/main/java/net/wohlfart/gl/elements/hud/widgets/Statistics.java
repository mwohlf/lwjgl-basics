package net.wohlfart.gl.elements.hud.widgets;


public class Statistics extends FormattedLabel {

    private static final float RENDER_INTERVAL = 1f;
    private float delta = 0;
    private int count = 0;


    public Statistics(int x, int y) {
        super(x, y, "fps:{0,number,#0.0} tpf:{1,number,#0.000}");
    }


    @Override
    public void update(float timeInSec) {
        delta += timeInSec;
        count += 1;
        if (delta > RENDER_INTERVAL) {
            setValue(new Object[] { count / delta, delta / count });
            delta = count = 0;
        }
        super.update(timeInSec);
    }

}
