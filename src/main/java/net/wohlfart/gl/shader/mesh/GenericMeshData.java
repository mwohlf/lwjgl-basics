package net.wohlfart.gl.shader.mesh;

import org.lwjgl.util.ReadableColor;

public interface GenericMeshData {

    int getVaoHandle();

    int getVboVerticesHandle();

    int getVboIndicesHandle();

    int getLineWidth();

    int getIndicesType();

    int getIndexElemSize();

    int getIndicesCount();

    int getIndexOffset();

    ReadableColor getColor();

}
