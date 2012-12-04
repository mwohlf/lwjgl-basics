package net.wohlfart.basic;

import org.lwjgl.util.vector.Matrix4f;


public interface IGameContext {

	<T> T getBeanOfType(Class<T> clazz);

}
