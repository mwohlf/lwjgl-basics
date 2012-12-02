package net.wohlfart.basic;

import java.util.Map.Entry;
import java.util.Set;


import org.lwjgl.LWJGLException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
	private static final String CONFIG_FILE = "config/applicationContext.xml";

	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(CONFIG_FILE);
		SpringContext context = new SpringContext(appContext);
		Game game = context.getBeanOfType(Game.class);
		game.setContext(context);
		game.start();
	}

}
