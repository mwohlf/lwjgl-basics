package net.wohlfart.basic.container;

import net.wohlfart.basic.elements.IsRenderable;

public interface RenderBatch<T extends IsRenderable> extends IsRenderable { // REVIEWED

    /**
     * to initialize stuff
     */
    void setup();

}
