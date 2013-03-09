package net.wohlfart.basic;

import java.util.Map.Entry;
import java.util.Set;

import org.springframework.context.ApplicationContext;

/**
 * <p>Wrapper class for the application context.</p>
 *
 */
class GameContext implements IGameContext {
    private final ApplicationContext delegate;

    GameContext(final ApplicationContext context) {
        this.delegate = context;
    }

    /** {@inheritDoc} */
    @Override
    public <T> T getBeanOfType(Class<T> clazz) {
        final Set<Entry<String, T>> set = delegate.getBeansOfType(clazz).entrySet();
        if (set.size() > 1) {
            throw new IllegalStateException("Multiple bean with type '" + clazz + "' found in application context, not sure which one to pick");
        }
        if (set.size() < 1) {
            throw new IllegalStateException("No bean with type '" + clazz + "' found in application context, can't start application");
        }
        final T t = set.iterator().next().getValue();
        return t;
    }

}
