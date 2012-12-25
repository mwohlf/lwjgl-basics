package net.wohlfart.tools;


import net.wohlfart.Assert;

import org.junit.Test;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public class SimpleMathTest {


	@Test
	public void testCreateQuaternionA() {
		Quaternion quat = new Quaternion();
		SimpleMath.createQuaternion(new Vector3f(0, 0, 1), new Vector3f(0, 1, 0), quat);

		Vector3f vec = new Vector3f(0, 0, 1);
		SimpleMath.mul(quat, vec, vec);
		Assert.assertEqualVec(new Vector3f(0, 1, 0), vec);
	}

	@Test
	public void testCreateQuaternionB() {
		Quaternion quat = new Quaternion();
		SimpleMath.createQuaternion(new Vector3f(1, 1, 0), new Vector3f(1, 0, 0), quat);

		Vector3f vec = new Vector3f(1, 1, 0);
		SimpleMath.mul(quat, vec, vec);
		Assert.assertEqualVec(new Vector3f(1.41421f, 0, 0), vec);
	}

	@Test
	public void testCreateQuaternionIdentity() {
		Quaternion quat = new Quaternion();
		SimpleMath.createQuaternion(new Vector3f(10, 0, 0), new Vector3f(10, 0, 0), quat);

		Vector3f vec = new Vector3f(1, 1, 0);
		SimpleMath.mul(quat, vec, vec);
		Assert.assertEqualVec(new Vector3f(1, 1, 0), vec);
	}

	@Test
	public void testCreateQuaternion1180degreeA() {
		Quaternion quat = new Quaternion();
		SimpleMath.createQuaternion(new Vector3f(10, 0, 0), new Vector3f(-10, 0, 0), quat);

		Vector3f vec = new Vector3f(3, 0, 0);
		SimpleMath.mul(quat, vec, vec);
		Assert.assertEqualVec(new Vector3f(-3, 0, 0), vec);
	}

	@Test
	public void testCreateQuaternion1180degreeB() {
		Quaternion quat = new Quaternion();
		SimpleMath.createQuaternion(new Vector3f(0, 1, 1), new Vector3f(0, -1, -1), quat);

		Vector3f vec = new Vector3f(3, 0, 0);
		SimpleMath.mul(quat, vec, vec);
		Assert.assertEqualVec(new Vector3f(-3, 0, 0), vec);
	}

	@Test
	public void testAddVector() {
		// for the sake of coverage
		Vector3f result = new Vector3f();
		SimpleMath.add(new Vector3f(0, 2, 1), new Vector3f(3, 0, 0), result);
		Assert.assertEqualVec(new Vector3f(3, 2, 1), result);
	}

}
