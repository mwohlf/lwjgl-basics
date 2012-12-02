package net.wohlfart.tools;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

@SuppressWarnings("serial")
public class SimpleMatrix4f extends Matrix4f {


	public static SimpleMatrix4f create(final Vector3f move, final Quaternion rot) {
		SimpleMatrix4f mat = new SimpleMatrix4f();

		float xx = rot.x * rot.x;
		float xy = rot.x * rot.y;
		float xz = rot.x * rot.z;
		float xw = rot.x * rot.w;

		float yy = rot.y * rot.y;
		float yz = rot.y * rot.z;
		float yw = rot.y * rot.w;

		float zz = rot.z * rot.z;
		float zw = rot.z * rot.w;

		// column-row syntax
	    mat.m00 = 1 - 2 * ( yy + zz );
	    mat.m10 =     2 * ( xy - zw );
	    mat.m20 =     2 * ( xz + yw );
	    mat.m30 =     move.x;

	    mat.m01 =     2 * ( xy + zw );
	    mat.m11 = 1 - 2 * ( xx + zz );
	    mat.m21 =     2 * ( yz - xw );
	    mat.m31 =     move.y;

	    mat.m02 =     2 * ( xz - yw );
	    mat.m12 =     2 * ( yz + xw );
	    mat.m22 = 1 - 2 * ( xx + yy );
	    mat.m32 =     move.z;

	    mat.m03 =     0;
	    mat.m13 =     0;
	    mat.m23 =     0;
	    mat.m33 =     1;

	    return mat;
	}


	/**
	 * @param move vector describing a move
	 * @return a matrix
	 */
	public static SimpleMatrix4f create(final Vector3f move) {
		SimpleMatrix4f mat = new SimpleMatrix4f();

		// column-row syntax
		mat.m00 = 1;
	    mat.m10 = 0;
	    mat.m20 = 0;
	    mat.m30 = move.x;

	    mat.m01 = 0;
	    mat.m11 = 1;
	    mat.m21 = 0;
	    mat.m31 = move.y;

	    mat.m02 = 0;
	    mat.m12 = 0;
	    mat.m22 = 1;
	    mat.m32 = move.z;

	    mat.m03 = 0;
	    mat.m13 = 0;
	    mat.m23 = 0;
	    mat.m33 = 1;

	    return mat;
	}


	/**
	 * @param rot quaternion describing a rotation
	 * @return a matrix
	 */
	public static SimpleMatrix4f create(final Quaternion rot) {
		SimpleMatrix4f mat = new SimpleMatrix4f();

		float xx = rot.x * rot.x;
		float xy = rot.x * rot.y;
		float xz = rot.x * rot.z;
		float xw = rot.x * rot.w;

		float yy = rot.y * rot.y;
		float yz = rot.y * rot.z;
		float yw = rot.y * rot.w;

		float zz = rot.z * rot.z;
		float zw = rot.z * rot.w;

		// column-row syntax
	    mat.m00 = 1 - 2 * ( yy + zz );
	    mat.m10 =     2 * ( xy - zw );
	    mat.m20 =     2 * ( xz + yw );
	    mat.m30 =     0;

	    mat.m01 =     2 * ( xy + zw );
	    mat.m11 = 1 - 2 * ( xx + zz );
	    mat.m21 =     2 * ( yz - xw );
	    mat.m31 =     0;

	    mat.m02 =     2 * ( xz - yw );
	    mat.m12 =     2 * ( yz + xw );
	    mat.m22 = 1 - 2 * ( xx + yy );
	    mat.m32 =     0;

	    mat.m03 =     0;
	    mat.m13 =     0;
	    mat.m23 =     0;
	    mat.m33 =     1;

	    return mat;
	}


}
