package net.wohlfart.gl.input;


public interface InputDispatcher {

    void register(Object listener);

    void unregister(Object listener);

    void post(Object event);

    void destroy();

}
