package net.wohlfart.gl.shader;

import net.wohlfart.gl.view.CanMove;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

public class LightSource implements CanMove {
    final Vector4f ambient;
    final Vector4f diffuse;
    final Vector4f specular;
    final Vector3f position;
    final Vector3f direction;


    public LightSource(Vector4f ambient, Vector4f diffuse, Vector4f specular, Vector3f position, Vector3f direction) {
        this.ambient = ambient;
        this.diffuse = diffuse;
        this.specular = specular;
        this.position = position;
        this.direction = direction;
    }

    public Vector4f getAmbient() {
        return ambient;
    }
    public Vector4f getDiffuse() {
        return diffuse;
    }
    public Vector4f getSpecular() {
        return specular;
    }
    @Override
    public Vector3f getPosition() {
        return position;
    }
    public Vector3f getDirection() {
        return direction;
    }

    @Override
    public void move(Vector3f vector) {
        // not supported
    }

    @Override
    public void setPosition(Vector3f vector) {
        position.set(vector);
    }
}
