package net.wohlfart.gl.shader;

/**
 * <p>IShaderProgram interface.</p>
 *
 *
 *
 */
public interface IShaderProgram {

    // to id the shader
    /**
     * <p>getProgramId.</p>
     *
     * @return a int.
     */
    abstract int getProgramId();

    // called when the shader in created
    /**
     * <p>setup.</p>
     */
    abstract void setup();

    // called when the shader program is about to be used
    /**
     * <p>bind.</p>
     */
    abstract void bind();

    // called when the shader is no longer used
    /**
     * <p>unbind.</p>
     */
    public abstract void unbind();

    // called when the shader is no longer needed
    /**
     * <p>dispose.</p>
     */
    public abstract void dispose();

}
