package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>DefaultShaderProgram class.</p>
 *
 *
 *
 */
public class DefaultShaderProgram extends AbstractShaderProgram {
    /** Constant <code>LOGGER</code> */
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultShaderProgram.class);

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

    private int vertexShaderId = -1;
    private int fragmentShaderId = -1;

    private final String vertexShader;
    private final String fragmentShader;

    /**
     * <p>Constructor for DefaultShaderProgram.</p>
     *
     * @param vertexShader a {@link java.lang.String} object.
     * @param fragmentShader a {@link java.lang.String} object.
     */
    public DefaultShaderProgram(String vertexShader, String fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "DefaultShaderProgram loaded from: \n" + vertexShader + "\n" + fragmentShader;
    }

    /** {@inheritDoc} */
    @Override
    public void setup() {
        super.setup();
        vertexShaderId = loadShader(vertexShader, GL20.GL_VERTEX_SHADER);
        fragmentShaderId = loadShader(fragmentShader, GL20.GL_FRAGMENT_SHADER);
        linkAndValidate(vertexShaderId, fragmentShaderId);
    }

    /** {@inheritDoc} */
    @Override
    public void dispose() {
        unlink(vertexShaderId, fragmentShaderId);
        super.dispose();
    }

}
