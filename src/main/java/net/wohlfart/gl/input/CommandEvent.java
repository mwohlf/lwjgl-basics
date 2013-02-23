package net.wohlfart.gl.input;

import net.wohlfart.tools.SimpleMath;

/*
 * the high level commands, base class for all kind of high level events/commands
 */
public class CommandEvent {
    private static final float ROTATION_SPEED = SimpleMath.TWO_PI;  // one rotation per sec
    private static final float MOVE_SPEED = 100f;                   // 100 units per sec

    protected float delta;


    public float getDelta() {
        return delta;
    }

    void setDelta(float delta) {
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

    protected static class KeybasedEvent extends CommandEvent {

        private int key;

        public void setTime(float time) {
            setDelta(1f * time);
        }

        void setKey(int key) {
            this.key = key;
        }

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
    }

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
}
