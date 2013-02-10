package net.wohlfart.gl.shader.mesh;

import java.util.List;

public interface Indices<T> {

    List<T> getContent();

    int getStructure(); //

    int getElemSize(); // size of a single index element

}
