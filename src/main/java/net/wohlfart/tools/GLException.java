package net.wohlfart.tools;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class GLException {

    public static void checkGLError() {
        final int errorValue = GL11.glGetError();
        if (errorValue != GL11.GL_NO_ERROR) {
            final String errorString = GLU.gluErrorString(errorValue);
            System.err.println("ERROR - " + errorString);
            System.exit(-1);
        }
    }

}
