package net.wohlfart.gl.elements.hud.txt;

import net.wohlfart.gl.elements.hud.Layer;


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
