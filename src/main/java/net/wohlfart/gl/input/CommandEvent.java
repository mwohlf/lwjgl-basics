package net.wohlfart.gl.input;

import net.wohlfart.tools.ObjectPool;
import net.wohlfart.tools.ObjectPool.PoolableObject;

/*
 * the high level commands, base class for all kind of high level events/commands
 */
/**
 * <p>CommandEvent class.</p>
 *
 *
 *
 */
public class CommandEvent implements PoolableObject  {

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
        CommandEvent result = pool.borrowObject();
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




    // ----------



    protected float delta;


    /**
     * <p>Getter for the field <code>delta</code>.</p>
     *
     * @return a float.
     */
    public float getDelta() {
        return delta;
    }

    protected void setDelta(float delta) {
        this.delta = delta;
    }


    protected static class PositionEvent extends CommandEvent {

        private int x;
        private int y;

        public void setPosition(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        @Override
        public String toString() {
            return this.getClass() + ": [delta=" + delta + ", x=" + x + ", y=" + y + "]";
        }
    }

    /*
    protected static class KeybasedEvent extends CommandEvent {

        private int key;

        public void setTime(float time) {
            setDelta(1f * time);
        }

        void setKey(int key) {
            this.key = key;
        }

        @Override
        int getKey() {
            return key;
        }

    }


    private static class RotationEvent extends KeybasedEvent {
        @Override
        public void setTime(float time) {
            super.setDelta(ROTATION_SPEED * time);
        }
    }

    private static class MoveEvent extends KeybasedEvent {
        @Override
        public void setTime(float time) {
            super.setDelta(MOVE_SPEED * time);
        }
        public void setWheelAmount(int amount) {
            super.setDelta(WHEEL_SENSITIVITY * amount);
        }
    }
    */

    /*
    // @formatter:off
    public static class PositionPointer extends PositionEvent {};

    public static class LeftClick extends PositionEvent {}

    public static class RightClick extends PositionEvent {}

    public static class MiddleClick extends PositionEvent {}

    public static class Null extends CommandEvent {}

    public static class Exit extends KeybasedEvent {}



    public static class RotateLeft extends RotationEvent {}

    public static class RotateRight extends RotationEvent {}

    public static class RotateUp extends RotationEvent {}

    public static class RotateDown extends RotationEvent {}

    public static class RotateClockwise extends RotationEvent {}

    public static class RotateCounterClockwise extends RotationEvent {}



    public static class MoveLeft extends MoveEvent {}

    public static class MoveRight extends MoveEvent {}

    public static class MoveUp extends MoveEvent {}

    public static class MoveDown extends MoveEvent {}

    public static class MoveForward extends MoveEvent {}

    public static class MoveBackward extends MoveEvent {}
    // @formatter:on
    */
}
