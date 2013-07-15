package net.wohlfart.gl.shader;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * properties for a material used in the shader
 */
public class Material {
    final Vector3f ambient;
    final Vector4f diffuse;
    final Vector3f specular;
    final float shininess;


    public Material(float shininess, Vector4f diffuse, Vector3f ambient, Vector3f specular) {
        this.shininess = shininess;
        this.specular = specular;
        this.diffuse = diffuse;
        this.ambient = ambient;
    }

}
