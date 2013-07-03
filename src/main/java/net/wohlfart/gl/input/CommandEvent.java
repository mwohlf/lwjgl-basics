package net.wohlfart.gl.input;

import net.wohlfart.tools.ObjectPool;
import net.wohlfart.tools.ObjectPool.PoolableObject;

/**
 * the high level commands, base class for all kind of high level events/commands
 */
public class CommandEvent implements PoolableObject {

    private CommandKey key;

    private static ObjectPool<CommandEvent> pool = new ObjectPool<CommandEvent>(10) {
        @Override
        protected CommandEvent newObject() {
            return new CommandEvent();
        }
    };

    public CommandKey getKey() {
        return key;
    }

    public static CommandEvent exit() {
        final CommandEvent result = pool.borrowObject();
        result.key = CommandKey.EXIT;
        return result;
    }

    @Override
    public void reset() {
        pool.returnObject(this);
    }

    public enum CommandKey {
        EXIT
    }

}
