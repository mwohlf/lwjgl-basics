package net.wohlfart.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import net.wohlfart.gl.CanMove;

import org.lwjgl.util.vector.Vector3f;

// this is the root of a scene graph
/**
 * <p>CelestialScene class.</p>
 *
 *
 *
 */
public class CelestialScene implements CanMove {

    protected final Vector3f position = new Vector3f();
    protected final Collection<Celestial> celestials = new ArrayList<Celestial>();

    /**
     * <p>Constructor for CelestialScene.</p>
     */
    public CelestialScene() {
        // celestials.add(new Celestial(){{position.set(new Vector3f(0,0,-40f));}});
    }

    /**
     * <p>setup.</p>
     */
    public void setup() {
        addRandomPlanets(13);
    }

    private void addRandomPlanets(final int planetCount) {
        final Random random = new Random(1);
        for (int i = 1; i <= planetCount; i++) {
            final Celestial celestial = new Celestial(random.nextLong());
            celestials.add(celestial);
            final float rad = (float) (2f * Math.PI) / planetCount * i;
            celestial.setPosition((float) Math.sin(rad) * 200, 0, (float) Math.cos(rad) * 200);
        }
    }

    /**
     * <p>update.</p>
     *
     * @param tpf a float.
     */
    public void update(float tpf) {
        for (final Celestial celestial : celestials) {
            celestial.update(tpf);
        }
    }

    /**
     * <p>render.</p>
     */
    public void render() {
        for (final Celestial celestial : celestials) {
            celestial.render();
        }
    }

    /**
     * <p>teardown.</p>
     */
    public void teardown() {

    }

    /** {@inheritDoc} */
    @Override
    public Vector3f getPosition() {
        return position;
    }

    /** {@inheritDoc} */
    @Override
    public void setPosition(Vector3f pos) {
        position.set(pos);
        for (final Celestial celestial : celestials) {
            celestial.position.set(celestial.position.x - position.x, celestial.position.y - position.y, celestial.position.z - position.z);
        }
        position.x = position.y = position.z = 0;
    }

}
