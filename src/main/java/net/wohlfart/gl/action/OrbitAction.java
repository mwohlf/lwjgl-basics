package net.wohlfart.gl.action;

import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Vector3f;

public class OrbitAction implements Action {

    private Vector3f center;
    private Vector3f axis;
    private float speed;

    private final Vector3f tmp1 = new Vector3f();
    private final Vector3f tmp2 = new Vector3f();

    private OrbitAction() {
        // use the factory method
    }

    public static OrbitAction create() {
        OrbitAction result = new OrbitAction();
        result.center = new Vector3f(0, 0, 0);
        result.axis = new Vector3f(0, 1, 0);
        result.speed = 1f/2f;
        return result;
    }

    public static OrbitAction create(final Vector3f center, final float speed) {
        OrbitAction result = new OrbitAction();
        result.center = center;
        result.axis = new Vector3f(0, 1, 0);
        result.speed = speed;
        return result;
    }

    @Override
    public void perform(Actor actor, float timeInSec) {
        Vector3f v = actor.getPosition();
        SimpleMath.sub(v, center, tmp1);
        float radius = tmp1.length();                // tmp1: from the center to the position

        Vector3f.cross(axis, tmp1, tmp2);            // tmp2: move direction
        tmp2.scale(timeInSec * speed);

        SimpleMath.add(tmp2, v, tmp1);               // tmp1: new position off the radius

        SimpleMath.sub(tmp1, center, tmp1);          // tmp1: direction from radius to new position
        tmp1.normalise(tmp1);

        v = SimpleMath.scale(tmp1, radius, v);
        actor.setPosition(v);
    }

}
