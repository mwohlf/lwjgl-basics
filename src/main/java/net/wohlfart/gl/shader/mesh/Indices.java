package net.wohlfart.gl.shader.mesh;

import java.util.List;

/**
 * <p>Indices interface.</p>
 *
 *
 *
 */
public interface Indices<T> {

    /**
     * <p>getContent.</p>
     *
     * @return a {@link java.util.List} object.
     */
    List<T> getContent();

    /**
     * <p>getStructure.</p>
     *
     * @return a int.
     */
    int getStructure(); //

    /**
     * <p>getElemSize.</p>
     *
     * @return a int.
     */
    int getElemSize(); // size of a single index element

}
