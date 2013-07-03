package net.wohlfart.basic.action;

import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public final class OrbitAction implements SpatialActor.Action {

    private float orbitTime; // in sec
    private Vector3f center;
    private Vector3f axis;

    private final Vector3f tmp1 = new Vector3f();
    private final Vector3f tmp2 = new Vector3f();

    private OrbitAction() {
        // use the factory method
    }

    public static OrbitAction create() {
        final OrbitAction result = new OrbitAction();
        result.orbitTime = 5f;
        result.center = new Vector3f(0, 0, 0);
        result.axis = new Vector3f(0, 1, 0);
        return result;
    }

    public static OrbitAction create(float orbitTime, Vector3f center) {
        final OrbitAction result = new OrbitAction();
        result.orbitTime = orbitTime;
        result.center = center;
        result.axis = new Vector3f(0, 1, 0);
        return result;
    }

    public static OrbitAction create(float orbitTime, Vector3f center, Vector3f axis) {
        final OrbitAction result = new OrbitAction();
        result.orbitTime = orbitTime;
        result.center = center;
        result.axis = axis;
        return result;
    }

    // FIXME: too many news in this method...
    @Override
    public void perform(SpatialActor spatialActor, float timeInSec) {
        Vector3f v = spatialActor.getPosition();
        SimpleMath.sub(v, center, tmp1);
        final float radius = tmp1.length(); // tmp1: from the center to the position

        //final Quaternion currentRotation = spatialActor.getRotation();

        // final Vector3f up = SimpleMath.getUp(currentRotation, new Vector3f()); // we want to keep up
        final Vector3f up = axis;
        final Vector3f right = new Vector3f(-tmp1.x, -tmp1.y, -tmp1.z).normalise(new Vector3f());
        final Vector3f forward = Vector3f.cross(up, right, new Vector3f());

        Vector3f.cross(axis, tmp1, tmp2); // tmp2: move direction
        tmp2.scale(timeInSec / orbitTime);

        final Quaternion rotation = SimpleMath.createQuaternion(new Vector3f(0, 0, 1), forward, new Quaternion());
        spatialActor.setRotation(rotation);

        SimpleMath.add(tmp2, v, tmp1); // tmp1: new position off the radius

        SimpleMath.sub(tmp1, center, tmp1); // tmp1: direction from radius to new position
        tmp1.normalise(tmp1);

        v = SimpleMath.scale(tmp1, radius, v);
        spatialActor.setPosition(v);
    }

}
