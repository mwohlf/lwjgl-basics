package net.wohlfart.gl;

import org.lwjgl.util.vector.Vector3f;

public interface CanMove {

    public Vector3f getPosition();

    public void setPosition(final Vector3f vector);

}
