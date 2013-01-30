package net.wohlfart.basic;

public interface IGameContext {
	<T> T getBeanOfType(Class<T> clazz);
}
