package net.wohlfart.basic;

import java.util.Map.Entry;
import java.util.Set;


import org.lwjgl.LWJGLException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
	private static final String CONFIG_FILE = "config/applicationContext.xml";

	public static void main(String[] args) {
		ApplicationContext context = new ClassPathXmlApplicationContext(CONFIG_FILE);
		Set<Entry<String, Game>> set = context.getBeansOfType(Game.class).entrySet();
		if (set.size() > 1) {
			throw new IllegalStateException("Multiple bean with type GameLoop found in application context, not sure which one to start");
		} if (set.size() < 1) {
			throw new IllegalStateException("No bean with type GameLoop found in application context, can't start application");
		}
		Game game = set.iterator().next().getValue();
		try {
			game.start();
		} catch (LWJGLException ex) {
			ex.printStackTrace();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
	}

}
