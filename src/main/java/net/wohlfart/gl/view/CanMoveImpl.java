package net.wohlfart.gl.view;

import org.lwjgl.util.vector.Vector3f;


@SuppressWarnings("serial")
public class CanMoveImpl extends Vector3f implements CanMove { // REVIEWED

    @Override
    public void move(Vector3f delta) {
        this.x += delta.x;
        this.y += delta.y;
        this.z += delta.z;
    }

    @Override
    public Vector3f getPosition() {
        return this;
    }

    @Override
    public void setPosition(Vector3f vector) {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

}
