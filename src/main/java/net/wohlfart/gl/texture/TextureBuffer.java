package net.wohlfart.gl.texture;

import java.nio.IntBuffer;

public interface TextureBuffer {

    void init();

    IntBuffer getBuffer();

    int getWidth();

    int getHeight();

}
