package net.wohlfart.tools;

import org.lwjgl.util.vector.Vector4f;

@SuppressWarnings("serial")
public class SimpleVector4f extends Vector4f {

    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + "," + w + ") ";
    }

}
