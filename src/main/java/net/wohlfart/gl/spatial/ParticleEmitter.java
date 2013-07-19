package net.wohlfart.gl.spatial;

import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.texture.TextureRegistry;
import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.shader.mesh.ParticleMesh;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL13;
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
        commonTextureId = TextureRegistry.TEXTURE_REGISTRY.getTextureHandle("/gfx/images/ash_uvgrid01.png", GL13.GL_TEXTURE0);

        final ParticleMesh.Builder builder = new ParticleMesh.Builder();
        builder.setFlippedFloatBuffer(getFlippedFloatBuffer());
        builder.setParticleCount(particles.size());
        builder.setTextureId(commonTextureId);
        builder.setInitRotation(initialRotation);
        builder.setInitTranslation(initialTranslation);
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


}
