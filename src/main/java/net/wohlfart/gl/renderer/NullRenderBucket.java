package net.wohlfart.gl.renderer;

import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;

public enum NullRenderBucket implements RenderBucket {
    INSTANCE;

    @Override
    public void render() {
            // do nothing
    }

    @Override
    public void destroy() {
        // do nothing
    }

    @Override
    public void update(float timeInSec) {
        // do nothing
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return SimpleMath.UNION_MATRIX;
    }

    @Override
    public Matrix4f getModelViewMatrix() {
        return SimpleMath.UNION_MATRIX;
    }

    @Override
    public void addContent(IsRenderable renderable) {
        // do nothing
    }

    @Override
    public void setup() {
        // do nothing
    }

}
