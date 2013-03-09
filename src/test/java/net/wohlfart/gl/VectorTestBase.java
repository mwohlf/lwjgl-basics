package net.wohlfart.gl;

import org.junit.Assert;
import org.lwjgl.util.vector.Vector3f;

/**
 * <p>VectorTestBase class.</p>
 *
 * @author michael
 * @version $Id: $Id
 * @since 0.0.1
 */
public class VectorTestBase {

    /**
     * <p>equals.</p>
     *
     * @param vec1 a {@link org.lwjgl.util.vector.Vector3f} object.
     * @param vec2 a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void equals(final Vector3f vec1, final Vector3f vec2) {
        Assert.assertEquals(vec1.x, vec2.x, 0.001f);
        Assert.assertEquals(vec1.y, vec2.y, 0.001f);
        Assert.assertEquals(vec1.z, vec2.z, 0.001f);
    }

}
