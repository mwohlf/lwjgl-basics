package net.wohlfart.gl.shader.mesh;

import java.util.List;


public interface Indices<T> {

	List<T> getContent();

	int getType();

}
