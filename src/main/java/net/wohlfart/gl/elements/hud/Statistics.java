package net.wohlfart.gl.elements.hud;


public class Statistics extends TextComponent {

    private float delta = 0;
    private int count = 0;
    private final float RENDER_INTERVAL = 1f;

    private final int y;
    private final int x;

    private FormattedLabel fpsLabel;
    private FormattedLabel tpfLabel;


    public Statistics(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void setLayer(Layer layer) {
        super.setLayer(layer);
        fpsLabel = new FormattedLabel(x, y, "fps: {0, number, 000.000}");
        fpsLabel.setLayer(getLayer());
        tpfLabel = new FormattedLabel(x, y, "tpf: {0, number, 000.000}");
        tpfLabel.setLayer(getLayer());
    }

    private void setFramesPerSecond(float fps) {
        fpsLabel.setValue(fps);
    }

    private void setTimePerFrame(float tpf) {
        tpfLabel.setValue(tpf);
    }

    public void update(float tpf) {
        delta += tpf;
        count += 1;
        if (delta > RENDER_INTERVAL) {
            setFramesPerSecond(count/delta);
            setTimePerFrame(delta/count);
            delta = count = 0;
        }
    }

    @Override
    public void render() {
        fpsLabel.render();
    }

    @Override
    public void dispose() {
        fpsLabel.dispose();
        tpfLabel.dispose();
    }

}
