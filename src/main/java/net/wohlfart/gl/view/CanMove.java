package net.wohlfart.gl.view;

import org.lwjgl.util.vector.Vector3f;


public interface CanMove { // REVIEWED

    public void move(Vector3f vector);


    public Vector3f getPosition();

    public void setPosition(Vector3f vector);

}
