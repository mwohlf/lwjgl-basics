package net.wohlfart.basic;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * <p>Application entry point.</p>
 */
public class Start {

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
        game.start();
    }

}
