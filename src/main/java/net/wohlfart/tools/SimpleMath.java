package net.wohlfart.tools;


public final class SimpleMath {

	public static final float TWO_PI = (float) (2 * Math.PI);
	public static final float QUARTER_PI = (float) (0.25 * Math.PI);
	public static final float HALF_PI = (float) (0.5 * Math.PI);
	public static final float PI = (float) (Math.PI);

	public static float sin(float f) {
		return (float)Math.sin(f);
	}

	public static float cos(float f) {
		return (float)Math.cos(f);
	}

	public static int random(int i) {
		return 0;
	}

	public static int random(int i, int j) {
		return 0;
	}

	public static float rad2deg(final float rad) {
		return (rad * 360f)/SimpleMath.TWO_PI;
	}

	public static float deg2rad(final float deg) {
		return (deg * SimpleMath.TWO_PI) / 360f;
	}

}
