package net.wohlfart.gl.input;

public interface InputSource {

    void createInputEvents(float delta);

    void destroy();

}
