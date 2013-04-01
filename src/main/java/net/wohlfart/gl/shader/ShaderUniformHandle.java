package net.wohlfart.gl.shader;

import static net.wohlfart.gl.shader.GraphicContextManager.INSTANCE;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix3f;
import org.lwjgl.util.vector.Matrix4f;

// handlers for uniforms used in the shader
/**
 * <p>ShaderUniformHandle class.</p>
 */
public enum ShaderUniformHandle { // @formatter:off
    MODEL_TO_WORLD("modelToWorldMatrix"),
    WORLD_TO_CAM("worldToCameraMatrix"),  // model view matrix
    CAM_TO_CLIP("cameraToClipMatrix"),    // projection matrix
    NORMAL("normalMatrix");
    // @formatter:on

    private final String lookupString;
    private final FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4);
    private final FloatBuffer matrix4Buffer = BufferUtils.createFloatBuffer(16);
    private final FloatBuffer matrix3Buffer = BufferUtils.createFloatBuffer(9);

    ShaderUniformHandle(String lookupString) {
        this.lookupString = lookupString;
    }

    String getLookupString() {
        return lookupString;
    }

    /**
     * <p>getLocation.</p>
     *
     * @return a int.
     */
    public int getLocation() {
        return INSTANCE.getCurrentGraphicContext().getLocation(this);
    }

    /**
     * <p>set.</p>
     *
     * @param matrix a {@link org.lwjgl.util.vector.Matrix4f} object.
     */
    public void set(Matrix4f matrix) {
        matrix.store(matrix4Buffer);
        matrix4Buffer.flip();
        GL20.glUniformMatrix4(getLocation(), false, matrix4Buffer);
    }

    /**
     * <p>set.</p>
     *
     * @param matrix a {@link org.lwjgl.util.vector.Matrix3f} object.
     */
    public void set(Matrix3f matrix) {
        matrix.store(matrix3Buffer);
        matrix3Buffer.flip();
        GL20.glUniformMatrix3(getLocation(), false, matrix3Buffer);
    }

    /**
     * <p>set.</p>
     *
     * @param readableColor a {@link org.lwjgl.util.ReadableColor} object.
     */
    public void set(ReadableColor readableColor) {
        colorBuffer.put(new float[] { // @formatter:off
                readableColor.getRed() / 255f,
                readableColor.getGreen() / 255f,
                readableColor.getBlue() / 255f,
                readableColor.getAlpha() / 255f, }); // @formatter:on
        colorBuffer.flip();
        set(colorBuffer);
    }

    /**
     * <p>set.</p>
     *
     * @param colorBuffer a {@link java.nio.FloatBuffer} object.
     */
    public void set(final FloatBuffer colorBuffer) {
        GL20.glUniform4(getLocation(), colorBuffer);
    }

}
