package net.wohlfart.gl.shader;




public interface IShaderProgram  {

	public static final String MODEL_MATRIX = "modelToWorldMatrix";
	public static final String VIEW_MATRIX = "worldToCameraMatrix";
	public static final String PROJECTION_MATRIX = "cameraToClipMatrix";
	public static final String UNI_COLOR = "uni_Color";

	// called when the shader in created
	public abstract void setup();

	// called when the shader program is about to be used
	public abstract void bind();

	// called when the shader is no longer used
	public abstract void unbind();

	// called when the shader is no longer needed
	public abstract void dispose();

}
