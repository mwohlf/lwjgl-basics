package net.wohlfart.gl.shader;

public class ShaderRegistry {

    private static final String DEFAULT_VERTEX_SHADER = "/shaders/default/vertex.glsl";
    private static final String DEFAULT_FRAGMENT_SHADER = "/shaders/default/fragment.glsl";

    private static final String WIREFRAME_VERTEX_SHADER = "/shaders/wireframe/vertex.glsl";
    private static final String WIREFRAME_FRAGMENT_SHADER = "/shaders/wireframe/fragment.glsl";

    private static final String HUD_VERTEX_SHADER = "/shaders/hud/vertex.glsl";
    private static final String HUD_FRAGMENT_SHADER = "/shaders/hud/fragment.glsl";

    private static final String LIGHTING01_VERTEX_SHADER = "/shaders/lighting01/vertex.glsl";
    private static final String LIGHTING01_FRAGMENT_SHADER = "/shaders/lighting01/fragment.glsl";

    private static final String LIGHTING02_VERTEX_SHADER = "/shaders/lighting02/vertex.glsl";
    private static final String LIGHTING02_FRAGMENT_SHADER = "/shaders/lighting02/fragment.glsl";

    private static final String LIGHTING03_VERTEX_SHADER = "/shaders/lighting03/vertex.glsl";
    private static final String LIGHTING03_FRAGMENT_SHADER = "/shaders/lighting03/fragment.glsl";

    private static final String LIGHTING_VERTEX_SHADER = "/shaders/lighting/vertex.glsl";
    private static final String LIGHTING_FRAGMENT_SHADER = "/shaders/lighting/fragment.glsl";

    private static final String FBO_VERTEX_SHADER = "/shaders/fbo/vertex.glsl";
    private static final String FBO_FRAGMENT_SHADER = "/shaders/fbo/fragment.glsl";

    private static final String FBO_BLUR_VERTEX_SHADER = "/shaders/fbo/blur_vertex.glsl";
    private static final String FBO_BLUR_FRAGMENT_SHADER = "/shaders/fbo/blur_fragment.glsl";



    // more complex light this shader needs some light sources otherwise the surfaces will stay black
    public static final IShaderProgram LIGHTING_SHADER = new DefaultShaderProgram(LIGHTING_VERTEX_SHADER, LIGHTING_FRAGMENT_SHADER);
    // simple lighting shader, light from one direction only
    public static final IShaderProgram LIGHTING01_SHADER = new DefaultShaderProgram(LIGHTING01_VERTEX_SHADER, LIGHTING01_FRAGMENT_SHADER);
    // simple lighting shader, light from one direction only
    public static final IShaderProgram LIGHTING02_SHADER = new DefaultShaderProgram(LIGHTING02_VERTEX_SHADER, LIGHTING02_FRAGMENT_SHADER);
    // better lighting, needs light sources
    public static final IShaderProgram LIGHTING03_SHADER = new DefaultShaderProgram(LIGHTING03_VERTEX_SHADER, LIGHTING03_FRAGMENT_SHADER);
    // no normals, no light source, just texture
    public static final IShaderProgram DEFAULT_SHADER = new DefaultShaderProgram(DEFAULT_VERTEX_SHADER, DEFAULT_FRAGMENT_SHADER);
    // wireframe only shader, planes  will be black, no shadow
    public static final IShaderProgram WIREFRAME_SHADER = new DefaultShaderProgram(WIREFRAME_VERTEX_SHADER, WIREFRAME_FRAGMENT_SHADER);
    // used in the hud
    public static final IShaderProgram HUD_SHADER = new DefaultShaderProgram(HUD_VERTEX_SHADER, HUD_FRAGMENT_SHADER);
    // used in the hud
    public static final IShaderProgram FBO_SHADER = new DefaultShaderProgram(FBO_VERTEX_SHADER, FBO_FRAGMENT_SHADER);
    public static final IShaderProgram FBO_BLUR_SHADER = new DefaultShaderProgram(FBO_BLUR_VERTEX_SHADER, FBO_BLUR_FRAGMENT_SHADER);

}
