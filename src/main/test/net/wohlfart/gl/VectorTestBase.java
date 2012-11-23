package net.wohlfart.gl;
import org.junit.Assert;
import org.lwjgl.util.vector.Vector3f;


public class VectorTestBase {

	public void equals(final Vector3f vec1, final Vector3f vec2) {
		Assert.assertEquals(vec1.x, vec2.x, 0.001f);
		Assert.assertEquals(vec1.y, vec2.y, 0.001f);
		Assert.assertEquals(vec1.z, vec2.z, 0.001f);
	}

}
