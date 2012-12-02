package net.wohlfart.tools;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GLException {

	public static void checkGLError() {
		int errorValue = GL11.glGetError();
		if (errorValue != GL11.GL_NO_ERROR) {
			String errorString = GLU.gluErrorString(errorValue);
			System.err.println("ERROR - " + errorString);
			System.exit(-1);
		}
	}

}
