package net.wohlfart.gl.spatial;

import java.util.Random;

import net.wohlfart.basic.elements.IsRenderable;
import net.wohlfart.basic.elements.SpatialEntity;
import net.wohlfart.gl.elements.AbstractRenderable;
import net.wohlfart.gl.texture.CelestialType;
import net.wohlfart.tools.SimpleMath;

public class CelestialBody extends AbstractRenderable implements SpatialEntity {
    private final int DEFAULT_LOD = 7;

    private final long seed;

    private final Random random;
    private final CelestialType planetType;

    private final float radius;

    private final float rotSpeed;

    private final float pathRadius;

    private final float pathArc;


    public CelestialBody(long seed) {
        this.seed = seed;
        this.random = new Random(seed);
        // random planet type
        final int index = random.nextInt(CelestialType.values().length);
        this.planetType = CelestialType.values()[index];
        this.radius = SimpleMath.random(planetType.minRadius, planetType.maxRadius);
        this.rotSpeed = SimpleMath.random(planetType.minRot, planetType.maxRot);
        this.pathRadius = SimpleMath.random(planetType.minPathRadius, planetType.maxPathRadius);
        this.pathArc = SimpleMath.random((float) -Math.PI, (float) Math.PI); // location on the path
    }


    @Override
    public void update(final float tpf) {

    }

    @Override
    public void destroy() {

    }




    @Override
    protected IsRenderable setupMesh() {
        final CelestialBodyMesh.RevolvedSphereBuilder builder = new CelestialBodyMesh.RevolvedSphereBuilder();
        builder.setLod(DEFAULT_LOD);
        builder.setCelestialType(planetType);
        builder.setRadius(radius);
        builder.setSeed(seed);
        return builder.build();
    }

}
