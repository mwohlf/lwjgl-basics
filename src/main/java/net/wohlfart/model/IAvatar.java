package net.wohlfart.model;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

public interface IAvatar {

    // moving and rotating
    public void translationForce(float x, float y, float z);

    public void rotationForce(float xRot, float yRot, float zRot);

    // getting the direction and position
    public Vector3f getDirection();

    public Vector3f getLeft();

    public Vector3f getUp();

    public Vector3f getWorldTranslation();

    // picking 2d/3d
    public Vector3f getWorldCoordinates(Vector2f vector2f, float f);

    // acting
    public void clickAction(Vector2f vector2f);

}
