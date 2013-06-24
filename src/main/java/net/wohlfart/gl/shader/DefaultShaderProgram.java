package net.wohlfart.gl.shader;

import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * DefaultShaderProgram class.
 * </p>
 */
public class DefaultShaderProgram extends AbstractShaderProgram {
    
    protected static final Logger LOGGER = LoggerFactory.getLogger(DefaultShaderProgram.class);

    private int vertexShaderId = -1;
    private int fragmentShaderId = -1;

    private final String vertexShader;
    private final String fragmentShader;

    /**
     * <p>
     * Constructor for DefaultShaderProgram.
     * </p>
     *
     * @param vertexShader
     *            a {@link java.lang.String} object.
     * @param fragmentShader
     *            a {@link java.lang.String} object.
     */
    public DefaultShaderProgram(String vertexShader, String fragmentShader) {
        this.vertexShader = vertexShader;
        this.fragmentShader = fragmentShader;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "DefaultShaderProgram loaded from: '" + vertexShader + "' and '" + fragmentShader +"'";
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
