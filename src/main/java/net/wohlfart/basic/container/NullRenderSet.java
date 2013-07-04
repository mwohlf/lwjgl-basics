package net.wohlfart.basic.container;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Matrix4f;

public enum NullRenderSet implements RenderSet<IsRenderable> {
    INSTANCE;

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
    public void setup() {
    }

}
