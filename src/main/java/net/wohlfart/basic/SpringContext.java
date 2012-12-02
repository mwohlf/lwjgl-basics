package net.wohlfart.basic;

import java.util.Set;
import java.util.Map.Entry;

import org.springframework.context.ApplicationContext;

public class SpringContext {
	private final ApplicationContext context;

	SpringContext(final ApplicationContext context) {
		this.context = context;
	}


	public <T> T getBeanOfType(Class<T> clazz) {
		Set<Entry<String, T>> set = context.getBeansOfType(clazz).entrySet();
		if (set.size() > 1) {
			throw new IllegalStateException("Multiple bean with type GameLoop found in application context, not sure which one to start");
		} if (set.size() < 1) {
			throw new IllegalStateException("No bean with type GameLoop found in application context, can't start application");
		}
		T t = set.iterator().next().getValue();
		return t;
	}



}
