package net.wohlfart.basic;

import net.wohlfart.basic.states.GameStateEnum;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>This is the application entry point.</p>
 */
public class Start { // REVIEWED

    /** path to the application config file */
    private static final String CONFIG_FILE = "config/applicationContext.xml";

    /**
     * <p>The main method.</p>
     *
     * @param args an array of {@link java.lang.String} objects, not used so far.
     */
    public static void main(String[] args) {
        final ApplicationContext appContext = new ClassPathXmlApplicationContext(CONFIG_FILE);
        final IGameContext context = new GameContext(appContext);
        final Game game = context.getBeanOfType(Game.class);
        for (GameStateEnum state : GameStateEnum.values()) {
            state.inject(context);
        }
        game.start();
    }

}
