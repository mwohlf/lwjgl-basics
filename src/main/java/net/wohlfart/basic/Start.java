package net.wohlfart.basic;

import net.wohlfart.basic.states.GameStateEnum;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * This is the application entry point.
 */
public class Start { // REVIEWED

    /** path to the application config file */
    private static final String CONFIG_FILE = "config/applicationContext.xml";

    public static void main(String[] args) {
        final ApplicationContext appContext = new ClassPathXmlApplicationContext(CONFIG_FILE);
        final IGameContext context = new GameContext(appContext);
        final Game game = context.getBeanOfType(Game.class);
        for (final GameStateEnum state : GameStateEnum.values()) {
            state.inject(context);
        }
        game.start();
    }

}
