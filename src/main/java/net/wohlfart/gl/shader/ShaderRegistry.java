package net.wohlfart.gl.shader;

public class ShaderRegistry {

    /** Constant <code>DEFAULT_VERTEX_SHADER="/shaders/default/vertex.glsl"</code> */
    public static final String DEFAULT_VERTEX_SHADER = "/shaders/default/vertex.glsl";
    /** Constant <code>DEFAULT_FRAGMENT_SHADER="/shaders/default/fragment.glsl"</code> */
    public static final String DEFAULT_FRAGMENT_SHADER = "/shaders/default/fragment.glsl";

    /** Constant <code>WIREFRAME_VERTEX_SHADER="/shaders/wireframe/vertex.glsl"</code> */
    public static final String WIREFRAME_VERTEX_SHADER = "/shaders/wireframe/vertex.glsl";
    /** Constant <code>WIREFRAME_FRAGMENT_SHADER="/shaders/wireframe/fragment.glsl"</code> */
    public static final String WIREFRAME_FRAGMENT_SHADER = "/shaders/wireframe/fragment.glsl";

    /** Constant <code>HUD_VERTEX_SHADER="/shaders/hud/vertex.glsl"</code> */
    public static final String HUD_VERTEX_SHADER = "/shaders/hud/vertex.glsl";
    /** Constant <code>HUD_FRAGMENT_SHADER="/shaders/hud/fragment.glsl"</code> */
    public static final String HUD_FRAGMENT_SHADER = "/shaders/hud/fragment.glsl";

    /** Constant <code>HUD_VERTEX_SHADER="/shaders/hud/vertex.glsl"</code> */
    public static final String LIGHTING_VERTEX_SHADER = "/shaders/lighting/vertex.glsl";
    /** Constant <code>HUD_FRAGMENT_SHADER="/shaders/hud/fragment.glsl"</code> */
    public static final String LIGHTING_FRAGMENT_SHADER = "/shaders/lighting/fragment.glsl";

    public static final IShaderProgram WIREFRAME_SHADER = new DefaultShaderProgram(WIREFRAME_VERTEX_SHADER, WIREFRAME_FRAGMENT_SHADER);
    public static final IShaderProgram DEFAULT_SHADER = new DefaultShaderProgram(DEFAULT_VERTEX_SHADER, DEFAULT_FRAGMENT_SHADER);
    public static final IShaderProgram HUD_SHADER = new DefaultShaderProgram(HUD_VERTEX_SHADER, HUD_FRAGMENT_SHADER);
    public static final IShaderProgram LIGHTING_SHADER = new DefaultShaderProgram(LIGHTING_VERTEX_SHADER, LIGHTING_FRAGMENT_SHADER);

}
