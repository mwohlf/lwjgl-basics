package net.wohlfart.gl.spatial;

import java.nio.FloatBuffer;
import java.util.HashSet;
import java.util.Set;

import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.mesh.ParticleMesh;
import net.wohlfart.tools.SimpleMath;
import net.wohlfart.tools.Vertex;

import org.lwjgl.BufferUtils;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ParticleEmitter extends AbstractRenderable implements Emitter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ParticleEmitter.class);

    Set<Particle> particles = new HashSet<Particle>();
    int newPerSecond; // new particles per second;
    float leftover; // fract from last update

    public ParticleEmitter() {
        this.newPerSecond = 2;
    }

    protected void add(int count) {
        for (int i = 0; i < count; i++) {
            particles.add(Particle.create(50, // lifetime
                    new Vector3f(0, 0, -40), // position
                    new Vector3f(SimpleMath.random(-0.5f, 0.5f), 1, SimpleMath.random(-0.5f, 0.5f)), // speed
                    new Vector3f(0, 0, 0) // acceleration
                    ));
        }
    }


    @Override
    protected IsRenderable setupMesh() {
        LOGGER.info("rendering {} particles", particles.size());

        final ParticleMesh.Builder builder = new ParticleMesh.Builder();
        builder.setFlippedFloatBuffer(getFlippedFloatBuffer());
        builder.setParticleCount(particles.size());
        builder.setTextureFilename("/gfx/images/ash_uvgrid01.png");
        return builder.build();
    }

    @Override
    public void update(float timeInSec) {
        leftover += timeInSec * newPerSecond;
        int newCount = (int) leftover;
        leftover = leftover - newCount;
        add(newCount);


        for (Particle particle : particles) {
            particle.update(timeInSec);
        }
        reCreateRenderable(true);
        super.update(timeInSec);
    }

    /**
     * the vertex stream has the following format:
     *
     * 4 position coords 4 normal coords 2 texture coords
     *
     *
     * @return
     */
    FloatBuffer getFlippedFloatBuffer() {
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(particles.size() * 6 * Vertex.elementCount);
        for (Particle particle : particles) {
            verticesBuffer.put(particle.getVerticesBuffer());
        }
        verticesBuffer.flip();
        return verticesBuffer;
    }


}
