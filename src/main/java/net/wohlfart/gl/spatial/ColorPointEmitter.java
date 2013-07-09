package net.wohlfart.gl.spatial;

import java.util.HashSet;
import java.util.Set;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.shader.mesh.SimpleMesh;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Vector3f;

public class ColorPointEmitter extends AbstractRenderable implements Emitter {

    Set<ColorPoint> colorPoints = new HashSet<ColorPoint>();
    int newPerSecond; // new particles per second;
    float leftover; // fract from last update

    public ColorPointEmitter(int newPerSecond) {
        this.newPerSecond = newPerSecond;
    }

    protected void add(int count) {
        for (int i = 0; i < count; i++) {
            colorPoints.add(ColorPoint.create(50, // lifetime
                    new Vector3f(0, 0, -4), // position
                    new Vector3f(SimpleMath.random(-0.5f, 0.5f), 1, SimpleMath.random(-0.5f, 0.5f)), // speed
                    new Vector3f(0, 0, 0) // acceleration
                    ));
        }
    }

    @Override
    protected IsRenderable setupMesh() {
        final SimpleMesh.Builder builder = new SimpleMesh.Builder();
        builder.setVertexStream(createVertexStream());
        builder.setParticleCount(colorPoints.size());
        return builder.build();
    }

    @Override
    public void update(float timeInSec) {
        leftover += timeInSec * newPerSecond;
        int newCount = (int) leftover;
        leftover = leftover - newCount;
        add(newCount);

        for (ColorPoint colorPoint : colorPoints) {
            colorPoint.update(timeInSec);
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
    float[] createVertexStream() {
        // TODO: check if the size of the attributes fits in the array
        int index = 0;// @formatter:off
        final float[] result = new float[colorPoints.size() * ( 3 + 4 ) ];
        for (final ColorPoint colorPoint : colorPoints) {
            result[index++] = colorPoint.getPosition().x;
            result[index++] = colorPoint.getPosition().y;
            result[index++] = colorPoint.getPosition().z;
            result[index++] = colorPoint.getColor().getRed();
            result[index++] = colorPoint.getColor().getGreen();
            result[index++] = colorPoint.getColor().getBlue();
            result[index++] = colorPoint.getColor().getAlpha();
        }  // @formatter:on
        return result;
    }


}
