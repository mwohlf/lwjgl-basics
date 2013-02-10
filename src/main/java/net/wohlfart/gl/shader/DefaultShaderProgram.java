package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DefaultShaderProgram extends AbstractShaderProgram {
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultShaderProgram.class);

    public static final String DEFAULT_VERTEX_SHADER = "/shaders/default/vertex.glsl";
    public static final String DEFAULT_FRAGMENT_SHADER = "/shaders/default/fragment.glsl";

    public static final String WIREFRAME_VERTEX_SHADER = "/shaders/wireframe/vertex.glsl";
    public static final String WIREFRAME_FRAGMENT_SHADER = "/shaders/wireframe/fragment.glsl";

    public static final String HUD_VERTEX_SHADER = "/shaders/hud/vertex.glsl";
    public static final String HUD_FRAGMENT_SHADER = "/shaders/hud/fragment.glsl";

    private int vertexShaderId = -1;
    private int fragmentShaderId = -1;

    private final String vertexShader;
    private final String fragmentShader;

    public DefaultShaderProgram(String vertexShader, String fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    @Override
    public String toString() {
        return "DefaultShaderProgram loaded from: \n" + vertexShader + "\n" + fragmentShader;
    }

    @Override
    public void setup() {
        super.setup();
        vertexShaderId = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
        linkAndValidate(vertexShaderId, fragmentShaderId);
    }

    @Override
    public void dispose() {
        unlink(vertexShaderId, fragmentShaderId);
        super.dispose();
    }

}
