package net.wohlfart.gl.shader;

import net.wohlfart.gl.view.CanMove;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;


/**
 * a light source that is applied in the vertex shader
 */
public class VertexLight implements CanMove {
    final float attenuation;
    final Vector4f diffuse;
    final Vector3f position;


    public VertexLight(float attenuation, Vector4f diffuse, Vector3f position) {
        this.attenuation = attenuation;
        this.diffuse = diffuse;
        this.position = position;
    }

    public float getAttenuation() {
        return attenuation;
    }
    public Vector4f getDiffuse() {
        return diffuse;
    }
    @Override
    public Vector3f getPosition() {
        return position;
    }
    @Override
    public void move(Vector3f vector) {
        Vector3f.add(position, vector, position);
    }
    @Override
    public void setPosition(Vector3f vector) {
        position.set(vector);
    }
}
