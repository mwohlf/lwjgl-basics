package net.wohlfart.gl.spatial;

import java.util.Random;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.SpatialEntity;
import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.texture.CelestialType;

public class CelestialBody extends AbstractRenderable implements SpatialEntity {
    private final int DEFAULT_LOD = 7;

    private final long seed;

    private final Random random;
    private final CelestialType planetType;

    private final float radius;


    public CelestialBody(long seed, CelestialType planetType, float radius) {
        this.seed = seed;
        this.random = new Random(seed);
        this.planetType = planetType;
        this.radius = radius;
    }

    public CelestialBody(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        this.planetType = CelestialType.values()[random.nextInt(CelestialType.values().length)];
        this.radius = random.nextFloat() * (planetType.maxRadius - planetType.minRadius) + planetType.minRadius;
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
        builder.setLod(DEFAULT_LOD);
        builder.setCelestialType(planetType);
        builder.setRadius(radius);
        builder.setSeed(seed);
        return builder.build();
    }

}
