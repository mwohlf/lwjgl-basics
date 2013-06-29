package net.wohlfart.gl.view;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;


public interface CanRotate { // REVIEWED

    public abstract void rotate(float deltaAngle, Vector3f axis);


    public abstract Quaternion getRotation();

    public abstract void setRotation(Quaternion quaternion);


    public abstract Vector3f getRght(Vector3f vector3f);

    public abstract Vector3f getUp(Vector3f result);

    public abstract Vector3f getForward(Vector3f result);

}
