package net.wohlfart.gl.spatial;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.shader.mesh.ParticleMesh;
import net.wohlfart.tools.PNGDecoder;
import net.wohlfart.tools.PNGDecoder.Format;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//
// we seriously need to optimize here...
// see: http://stackoverflow.com/questions/10697161/why-floatbuffer-instead-of-float
// see: http://www.java-gaming.org/index.php?topic=22699.0
//
public class ParticleEmitter extends AbstractRenderable implements Emitter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParticleEmitter.class);
    public static final int MAX_PARTICLES = 500;

    private final Set<Particle> particles = new HashSet<Particle>(MAX_PARTICLES);
    private final int newPerSecond; // new particles per second;
    private float leftover; // fraction from last update
    private Integer commonTextureId;

    private final FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(MAX_PARTICLES * 6 * (3 + 4 + 3 + 2));


    public ParticleEmitter() {
        this.newPerSecond = 5;
    }

    protected void add(int count) {
        for (int i = 0; i < count; i++) {
            if (particles.size() > MAX_PARTICLES) {
                return;
            }
            particles.add(Particle.create(10, // lifetime
                    new Vector3f(0, 0, 0), // position
                    new Vector3f(SimpleMath.random(-0.5f, 0.5f), 3f, SimpleMath.random(-0.5f, 0.5f)), // speed
                    new Vector3f(0, -0.3f, 0) // acceleration
                    ));
        }
    }


    @Override
    protected IsRenderable setupMesh() {
        LOGGER.debug("rendering {} particles", particles.size());
        if (commonTextureId == null) {
            commonTextureId  = loadPNGTexture("/gfx/images/ash_uvgrid01.png", GL13.GL_TEXTURE0);
        }

        final ParticleMesh.Builder builder = new ParticleMesh.Builder();
        builder.setFlippedFloatBuffer(getFlippedFloatBuffer());
        builder.setParticleCount(particles.size());
        builder.setTextureId(commonTextureId);
        return builder.build();
    }

    @Override
    public void update(float timeInSec) {
        leftover += timeInSec * newPerSecond;
        int newCount = (int) leftover;
        leftover = leftover - newCount;
        add(newCount);

        Iterator<Particle> iterator = particles.iterator();
        while (iterator.hasNext()) {
            Particle particle = iterator.next();
            if (!particle.isAlive()) {
                iterator.remove();
                particle.reset();
            }
            particle.update(timeInSec, getPosition());
        }
        reCreateRenderable(true);
        super.update(timeInSec);
    }

    /**
     * the vertex stream has the following format:
     * 3 position coords
     * 4 color coords
     * 3 normal coords
     * 2 texture coords
     */
    FloatBuffer getFlippedFloatBuffer() {
        verticesBuffer.clear();
        for (Particle particle : particles) {
            verticesBuffer.put(particle.getVerticesBuffer());
        }
        verticesBuffer.flip();
        return verticesBuffer;
    }


    private int loadPNGTexture(String filename, int textureUnit) {
        int texId = 0;

        // InputStream inputStream = new FileInputStream(filename);
        try (InputStream inputStream = ClassLoader.class.getResourceAsStream(filename)) {

            // Link the PNG decoder to this stream
            final PNGDecoder decoder = new PNGDecoder(inputStream);
            // Get the width and height of the texture
            final int tWidth = decoder.getWidth();
            final int tHeight = decoder.getHeight();
            // Decode the PNG file in a ByteBuffer
            final ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
            buffer.flip();

            // Create a new texture object in memory and bind it
            texId = GL11.glGenTextures();
            GL13.glActiveTexture(textureUnit);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texId);
            // All RGB bytes are aligned to each other and each component is 1 byte
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            // Upload the texture data and generate mip maps (for scaling)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, tWidth, tHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            // Setup the ST coordinate system
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            // Setup what to do when the texture has to be scaled
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

        } catch (final FileNotFoundException ex) {
            LOGGER.error("can't load texture image", ex);
        } catch (final IOException ex) {
            LOGGER.error("can't load texture image", ex);
        }
        return texId;
    }

}
