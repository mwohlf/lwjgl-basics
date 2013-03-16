package net.wohlfart.model;

import java.util.Random;

import net.wohlfart.gl.texture.CelestialTexture;
import net.wohlfart.gl.texture.CelestialType;
import net.wohlfart.gl.texture.ITexture;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.glu.Sphere;
import org.lwjgl.util.vector.Vector3f;

class Celestial {

    protected static final int LOD = 64;

    protected final Sphere sphere = new Sphere();
    protected final Vector3f position = new Vector3f();
    protected final float radius;
    // protected final IntBuffer texture;

    private final ITexture texture;
    private final long seed;
    private final Random random;
    private final CelestialType planetType;
    private final float pathArc;
    private final float pathRadius;
    private final float rotSpeed;
    private final Vector3f rotAxis;

    /**
     * <p>Constructor for Celestial.</p>
     *
     * @param seed a long.
     */
    public Celestial(long seed) {
        this.seed = seed;
        this.random = new Random(seed);

        // random planet type
        final int index = random.nextInt(CelestialType.values().length);
        planetType = CelestialType.values()[index];

        radius = getRandom(planetType.minRadius, planetType.maxRadius);

        rotSpeed = getRandom(planetType.minRot, planetType.maxRot);

        pathRadius = getRandom(planetType.minPathRadius, planetType.maxPathRadius);

        pathArc = getRandom((float) -Math.PI, (float) Math.PI); // location on the path

        final float f = planetType.maxAxisDeplacement;
        // rotAxis = new Vector3f(0,0,1);
        rotAxis = new Vector3f(getRandom(-f, f), getRandom(-f, f), 1);

        sphere.setDrawStyle(GLU.GLU_FILL);
        sphere.setNormals(GLU.GLU_SMOOTH);
        sphere.setOrientation(GLU.GLU_OUTSIDE);

        texture = new CelestialTexture(radius, planetType, 2);
        texture.init();
    }

    /**
     * <p>Setter for the field <code>position</code>.</p>
     *
     * @param position a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    public void setPosition(final Vector3f position) {
        this.position.set(position);
    }

    /**
     * <p>Setter for the field <code>position</code>.</p>
     *
     * @param x a float.
     * @param y a float.
     * @param z a float.
     */
    public void setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
    }

    /**
     * <p>render.</p>
     */
    public void render() {
        texture.bind();

        GL11.glPushMatrix();
        GL11.glTranslatef(position.x, position.y, position.z);
        GL11.glColor3f(0.1f, 0.4f, 0.9f);
        GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);

        sphere.setDrawStyle(GLU.GLU_FILL);
        sphere.setNormals(GLU.GLU_SMOOTH);
        sphere.setOrientation(GLU.GLU_OUTSIDE);
        sphere.setTextureFlag(true);
        sphere.draw(radius, LOD, LOD);
        GL11.glPopMatrix();
    }

    /**
     * <p>update.</p>
     *
     * @param tpf a float.
     */
    public void update(final float tpf) {

    }

    /**
     * <p>distroy.</p>
     */
    public void distroy() {

    }

    private float getRandom(float min, float max) {
        return (1f - random.nextFloat()) * (max - min) + min;
    }

    /**
     * <p>Getter for the field <code>seed</code>.</p>
     *
     * @return a long.
     */
    protected long getSeed() {
        return seed;
    }

    /**
     * <p>Getter for the field <code>rotAxis</code>.</p>
     *
     * @return a {@link org.lwjgl.util.vector.Vector3f} object.
     */
    protected Vector3f getRotAxis() {
        return rotAxis;
    }

    /**
     * <p>Getter for the field <code>radius</code>.</p>
     *
     * @return a float.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * <p>Getter for the field <code>rotSpeed</code>.</p>
     *
     * @return a float.
     */
    public float getRotSpeed() {
        return rotSpeed;
    }

    /**
     * <p>getType.</p>
     *
     * @return a {@link net.wohlfart.gl.texture.CelestialType} object.
     */
    public CelestialType getType() {
        return planetType;
    }

    /**
     * <p>Getter for the field <code>pathRadius</code>.</p>
     *
     * @return a float.
     */
    public float getPathRadius() {
        return pathRadius;
    }

    /**
     * <p>Getter for the field <code>pathArc</code>.</p>
     *
     * @return a float.
     */
    public float getPathArc() {
        return pathArc;
    }

}
