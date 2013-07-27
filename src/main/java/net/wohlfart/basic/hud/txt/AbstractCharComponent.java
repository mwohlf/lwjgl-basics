package net.wohlfart.basic.hud.txt;

import net.wohlfart.basic.hud.Layer;


public abstract class AbstractCharComponent implements Layer.Widget {

    private Layer layer;

    @Override
    public void setLayer(Layer layer) {
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }

}
