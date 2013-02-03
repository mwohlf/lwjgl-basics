package net.wohlfart.gl;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public interface CanRotate {

	public abstract void rotate(final float angle, final Vector3f axis);

	public abstract Quaternion getRotation();

	public abstract Vector3f getRght(final Vector3f vector3f);

	public abstract Vector3f getUp(final Vector3f result);

	public abstract Vector3f getDir(final Vector3f result);

}
