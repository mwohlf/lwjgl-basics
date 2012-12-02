package net.wohlfart.gl;

import org.lwjgl.util.vector.Vector3f;

@SuppressWarnings("serial")
public class CanMoveImpl extends Vector3f implements CanMove {

	@Override
	public Vector3f getPos() {
		return this;
	}

	@Override
	public void setPos(final Vector3f vector) {
		this.x = vector.x;
		this.y = vector.y;
		this.z = vector.z;
	}

}
