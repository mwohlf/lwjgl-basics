package net.wohlfart.tools;




import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

public class ShaderUtils {

	public static int VERTEX = GL20.GL_VERTEX_SHADER;
	public static int FRAGMENT = GL20.GL_FRAGMENT_SHADER;


	/**
	 * create a buffer for a int array
	 * 
	 * @param buffer
	 * @return
	 */
	public static IntBuffer createBuffer(final int[] buffer) {
		IntBuffer result = BufferUtils.createIntBuffer(buffer.length);
		for(int i:buffer) {
			result.put(i);
		}
		result.rewind();
		return result;
	}

	/**
	 * create a buffer for a float array
	 * 
	 * @param buffer
	 * @return
	 */
	public static FloatBuffer createBuffer(final float[] buffer) {
		FloatBuffer result = BufferUtils.createFloatBuffer(buffer.length);
		for(float f:buffer) {
			result.put(f);
		}
		result.rewind();
		return result;
	}

	/**
	 * create a buffer for a matrix
	 * 
	 * @param matrix
	 * @return
	 */
	public static FloatBuffer createBuffer(final Matrix4f matrix) {
		float[] m = {
			matrix.m00,matrix.m01,matrix.m02,matrix.m03,
			matrix.m10,matrix.m11,matrix.m12,matrix.m13,
			matrix.m20,matrix.m21,matrix.m22,matrix.m23,
			matrix.m30,matrix.m31,matrix.m32,matrix.m33
		};
		return createBuffer(m);
	}
	
	/**
	 * 
	 * @param target
	 * @param bufferdata
	 * @return
	 */
	public static int makeBuffer(final int target, final IntBuffer bufferdata) {
		IntBuffer bufferid = BufferUtils.createIntBuffer(1);
		GL15.glGenBuffers(bufferid);
		int buffer = bufferid.get(0);
		GL15.glBindBuffer(target, buffer);
		GL15.glBufferData(target, bufferdata, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(target, 0);
		return buffer;
	}
	
	public static int makeBuffer(final int target, final FloatBuffer bufferdata) {
		IntBuffer bufferid = BufferUtils.createIntBuffer(1);
		GL15.glGenBuffers(bufferid);
		int buffer = bufferid.get(0);
		GL15.glBindBuffer(target, buffer);
		GL15.glBufferData(target, bufferdata, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(target, 0);
		return buffer;
	}


	/**
	 * Sets opengl to use the specified program.
	 * @param id The program to use.
	 */
	public static void useProgram(int id) {
		GL20.glUseProgram(id);
		GLException.checkGLError();
	}
	
	/**
	 * Sets opengl to use the default shaders.
	 */
	public static void useFixedFunctions() {
		GL20.glUseProgram(0);
		GLException.checkGLError();
	}
	
	/**
	 * Creates a program containing the specified shaders.
	 * @param shaders The shaders to be used
	 * @return The id of the program.
	 */
	public static int makeProgram(int... shaders) {
		int id = GL20.glCreateProgram();
		for(int shader: shaders) {
			GL20.glAttachShader(id, shader);
			GLException.checkGLError();
		}
		GL20.glLinkProgram(id);
		GLException.checkGLError();
		return id;
	}
	
	/**
	 * Returns the position of the specified uniform variable.
	 * @param program The program containing the variable.
	 * @param var The name of the variable.
	 * @return  The position of the variable.
	 */
	public static int getUniformVarPos(int program, String var) {
		int i = GL20.glGetUniformLocation(program, var);
		GLException.checkGLError();
		return i;
	}
	
	/**
	 * Sets the value of the specified uniform variable to that given.
	 * @param program The id of the program containing the variable.
	 * @param name The name of the variable.
	 * @param values The value or values to be set.
	 */
	public static void setUniformVar(int program, String name, float... values) {
		int loc = GL20.glGetUniformLocation(program, name);
		GLException.checkGLError();
		switch(values.length) {
		case 1: {GL20.glUniform1f(loc, values[0]); break;}
		case 2: {GL20.glUniform2f(loc, values[0], values[1]); break;}
		case 3: {GL20.glUniform3f(loc, values[0], values[1], values[2]); break;}
		case 4: {GL20.glUniform4f(loc, values[0], values[1], values[2], values[3]); break;}
		default: {
			FloatBuffer buff = BufferUtils.createFloatBuffer(values.length);
			buff.put(values);
			buff.rewind();
			GL20.glUniform1(loc, buff);
		}
		}
		GLException.checkGLError();
	}
	
	/**
	 * Sets the value of the specified uniform variable to that given.
	 * @param program The id of the program containing the variable.
	 * @param name The name of the variable.
	 * @param values The value or values to be set.
	 */
	public static void setUniformVar(int program, String name, int... values) {
		int loc = GL20.glGetUniformLocation(program, name);
		GLException.checkGLError();
		switch(values.length) {
		case 1: {GL20.glUniform1i(loc, values[0]); break;}
		case 2: {GL20.glUniform2i(loc, values[0], values[1]); break;}
		case 3: {GL20.glUniform3i(loc, values[0], values[1], values[2]); break;}
		case 4: {GL20.glUniform4i(loc, values[0], values[1], values[2], values[3]); break;}
		default: {
			IntBuffer buff = BufferUtils.createIntBuffer(values.length);
			buff.put(values);
			buff.rewind();
			GL20.glUniform1(loc, buff);
		}
		}
		GLException.checkGLError();
	}
	
	/**
	 * Creates and compiles a shader of the given type with the given source. If the shader fails to compile, the error message is
	 * printed and -1 is returned.
	 * @param source A string containing the shader source.
	 * @param type The type of the shader, either ShaderUtils.VERTEX or ShaderUtils.FRAGMENT (copies of GL20.GL_VERTEX_SHADER etc.)
	 * @return The id of the shader or -1 if the compiling fails.
	 */
	public static int makeShader(String source, int type) {
		int id = GL20.glCreateShader(type);
		GLException.checkGLError();
		GL20.glShaderSource(id, source);
		GLException.checkGLError();
		GL20.glCompileShader(id);
		String s = GL20.glGetShaderInfoLog(id, 1000);
		if(!s.isEmpty()) {
			System.err.println("Error compiling " + source);
			System.err.println(s);
			return -1;
		}
		GLException.checkGLError();
		return id;
	}
	
	/**
	 * Allows GLSL fragment shaders to use different colours for front and back facing colours.
	 * Without this, gl_BackColor in the vertex shader is ignored. It is be disabled by default.
	 */
	public static void enableTwoSide() {
		GL11.glEnable(GL20.GL_VERTEX_PROGRAM_TWO_SIDE);
	}
	/**
	 * Disallows GLSL fragment shaders to use different colours for front and back facing colours.
	 * With this, gl_BackColor in the vertex shader is ignored. It is be disabled by default.
	 */
	public static void disableTwoSide() {
		GL11.glDisable(GL20.GL_VERTEX_PROGRAM_TWO_SIDE);
	}
	/**
	 * Allows GLSL vertex shaders to set the size of the points draw.
	 */
	public static void enablePointSize() {
		GL11.glEnable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
	}
	/**
	 * Disallows GLSL vertex shaders to set the size of the points draw.
	 */
	public static void disablePointSize() {
		GL11.glDisable(GL20.GL_VERTEX_PROGRAM_POINT_SIZE);
	}
	/**
	 * Loads a file located in resoures/shaders and returns the content in a string.
	 * @param name The name of the file.
	 * @return A string containing the context of the file.
	 */
	public static String loadText(String name) {
		String seperator = File.separator;
		String s = "resources" + seperator + "shaders" + seperator + name;
		try {
			try(BufferedReader br = new BufferedReader(new FileReader(new File(ShaderUtils.class.getClassLoader().getResource(s).toURI())));) {
				s = "";
				String line = br.readLine();
				while(line != null) {
					s += line;
					s += "\n";
					line = br.readLine();
				}
			}
		} catch (IOException | URISyntaxException ex) {
			Logger.getLogger(ShaderUtils.class.getName()).log(Level.SEVERE, null, ex);
		}
		return s;
	}
	/**
	 * @return A string representing the maximum possible version of GLSL obtainable in the current gl context.
	 */
	public static String getMaxGLSLVersion() {
		return GL11.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION);
	}
}

