package net.wohlfart.gl.spatial;

import java.util.HashSet;
import java.util.Set;

import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.shader.ShaderAttributeHandle;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Vector3f;

public class ParticleEmitter extends AbstractRenderable {

    Set<Particle> particles = new HashSet<Particle>();
    int newPerSecond;        // new particles per second;
    float leftover;   // fract from  last update

    public ParticleEmitter(int newPerSecond) {
        this.newPerSecond = newPerSecond;
    }


    protected void add(int count) {
        for (int i = 0; i < count ; i++) {
            particles.add(Particle.create(50,  // lifetime
                    new Vector3f(0, 0, -4),   // position
                    new Vector3f(SimpleMath.random(-0.5f, 0.5f), 1, SimpleMath.random(-0.5f, 0.5f)),         // speed
                    new Vector3f(0, 0, 0)          // acceleration
            ));
        }
    }


    @Override
    protected IsRenderable setupMesh() {
        final ParticleMeshBuilder builder = new ParticleMeshBuilder();
        builder.setVertexStream(createVertexStream());
        builder.setParticleCount(particles.size());
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

        System.out.println("particles: " + particles.size());
    }


    /**
     * the vertex stream has the following format:
     *
     * 4 position coords 4 normal coords 2 texture coords
     *
     *
     * @return
     */
    float[] createVertexStream() {
        // TODO: check if the size of the attributes fits in the array
        int index = 0;  // @formatter:off
        final float[] result = new float[particles.size() * (
                  ShaderAttributeHandle.POSITION.getFloatCount()
                + ShaderAttributeHandle.COLOR.getFloatCount()) ];
        for (final Particle particle : particles) {
            result[index++] = particle.getPosition().x;
            result[index++] = particle.getPosition().y;
            result[index++] = particle.getPosition().z;
            result[index++] = 1;
            result[index++] = particle.getColor().getRed();
            result[index++] = particle.getColor().getGreen();
            result[index++] = particle.getColor().getBlue();
            result[index++] = particle.getColor().getAlpha();
        }
        return result;
    }


}
