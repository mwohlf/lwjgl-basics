package net.wohlfart.tools;

import org.lwjgl.util.vector.Vector4f;

/**
 * <p>SimpleVector4f class.</p>
 *
 *
 *
 */
@SuppressWarnings("serial")
public class SimpleVector4f extends Vector4f {

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "(" + x + "," + y + "," + z + "," + w + ") ";
    }

}
