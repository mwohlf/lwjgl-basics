package net.wohlfart.basic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Start {
	private static final String CONFIG_FILE = "config/applicationContext.xml";


	public static void main(String[] args) {
		ApplicationContext appContext = new ClassPathXmlApplicationContext(CONFIG_FILE);
		IGameContext context = new GameContext(appContext);
		Game game = context.getBeanOfType(Game.class);
		game.start();
	}

}
