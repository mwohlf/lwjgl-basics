package net.wohlfart.gl.shader;

import static net.wohlfart.gl.shader.GraphicContextManager.INSTANCE;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix4f;

// handlers for uniforms used in the shader
public enum ShaderUniformHandle { // @formatter:off
    MODEL_TO_WORLD("modelToWorldMatrix"),
    WORLD_TO_CAM("worldToCameraMatrix"),
    CAM_TO_CLIP("cameraToClipMatrix");
    // @formatter:on

    private final String lookupString;
    private final FloatBuffer colorBuffer = BufferUtils.createFloatBuffer(4);
    private final FloatBuffer matrixBuffer = BufferUtils.createFloatBuffer(16);

    ShaderUniformHandle(String lookupString) {
        this.lookupString = lookupString;
    }

    String getLookupString() {
        return lookupString;
    }

    public int getLocation() {
        return INSTANCE.getCurrentGraphicContext().getLocation(this);
    }

    public void set(Matrix4f matrix) {
        matrix.store(matrixBuffer);
        matrixBuffer.flip();
        GL20.glUniformMatrix4(getLocation(), false, matrixBuffer);
    }

    public void set(ReadableColor readableColor) {
        colorBuffer.put(new float[] { // @formatter:off
                readableColor.getRed() / 255f,
                readableColor.getGreen() / 255f,
                readableColor.getBlue() / 255f,
                readableColor.getAlpha() / 255f, }); // @formatter:on
        colorBuffer.flip();
        set(colorBuffer);
    }

    public void set(final FloatBuffer colorBuffer) {
        GL20.glUniform4(getLocation(), colorBuffer);
    }

}
