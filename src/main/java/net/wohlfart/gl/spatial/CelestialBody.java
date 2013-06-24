package net.wohlfart.gl.spatial;

import java.util.Random;

import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.texture.CelestialType;
import net.wohlfart.tools.SimpleMath;

public class CelestialBody extends AbstractRenderable  implements Spatial {

    private final long seed;
    private Random random;
    private final CelestialType planetType;


    public CelestialBody(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        // random planet type
        final int index = random.nextInt(CelestialType.values().length);
        this.planetType = CelestialType.values()[index];
    }


    @Override
    public void update(final float tpf) {

    }

    @Override
    public void destroy() {

    }




    @Override
    protected IsRenderable setupMesh() {
        final CelestialBodyMesh.Builder builder = new CelestialBodyMesh.Builder();
        this.random = new Random(seed);
        builder.setRadius(SimpleMath.random(planetType.minRadius, planetType.maxRadius));
        return builder.build();

    }

}
