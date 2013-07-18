package net.wohlfart.basic.container;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;

public enum NullRenderBatch implements RenderBatch<IsRenderable> {
    INSTANCE;

    @Override
    public void setup() {
    }

    @Override
    public void render() {
    }

    @Override
    public void destroy() {
    }

    @Override
    public Matrix4f getModelViewMatrix() {
        return SimpleMath.UNION_MATRIX;
    }

    @Override
    public Matrix4f getProjectionMatrix() {
        return SimpleMath.UNION_MATRIX;
    }

}
