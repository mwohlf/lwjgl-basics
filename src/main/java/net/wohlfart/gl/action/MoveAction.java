package net.wohlfart.gl.action;

import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Vector3f;

public final class MoveAction implements Action {

    private float speed; // the time for moving one unit
    private Vector3f direction;

    private final Vector3f tmp = new Vector3f();

    private MoveAction() {
        // use the factory method
    }

    public static MoveAction create() {
        final MoveAction result = new MoveAction();
        result.speed = 10f;
        result.direction = new Vector3f(0, 0, -1).normalise(null);
        return result;
    }

    public static MoveAction create(float speed, Vector3f direction) {
        final MoveAction result = new MoveAction();
        result.speed = speed;
        result.direction = direction.normalise(result.direction);
        return result;
    }

    @Override
    public void perform(Actor actor, float time) {
        final Vector3f v = actor.getPosition();
        SimpleMath.scale(direction, time / speed, tmp);
        SimpleMath.add(v, tmp, v);
        actor.setPosition(v);
    }

}
