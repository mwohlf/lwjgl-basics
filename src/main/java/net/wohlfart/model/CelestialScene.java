package net.wohlfart.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.wohlfart.gl.CanMove;

import org.lwjgl.util.vector.Vector3f;


public class CelestialScene implements CanMove {

	protected final Vector3f root = new Vector3f();
	protected final List<Celestial> celestials = new ArrayList<Celestial>();

	public CelestialScene() {
		//celestials.add(new Celestial(){{position.set(new Vector3f(0,0,-40f));}});

        // FIXME: this is ad-hoc init of a solar system remove in production...
        final int planetCount = 13;
        final Random random = new Random(1);
        for (int i = 1; i <= planetCount; i++) {
            final Celestial celestial = new Celestial(random.nextLong());
            celestials.add(celestial);
            final float rad = ((float)(2f * Math.PI) / (float) planetCount) * (float) i;
            celestial.setPosition((float)Math.sin(rad) * 200, (float)0, (float)Math.cos(rad) * 200);
        }
	}

	public void update(float tpf) {
		for (Celestial celestial : celestials) {
			celestial.position.set(
					celestial.position.x - root.x,
					celestial.position.y - root.y,
					celestial.position.z - root.z);
			celestial.update(tpf);
		}
		root.x = root.y = root.z = 0;
	}

	public void render() {
		for (Celestial celestial : celestials) {
			celestial.render();
		}
	}

	public void teardown() {

	}

	@Override
	public Vector3f getPos() {
		return root;
	}

	@Override
	public void setPos(Vector3f pos) {
		root.set(pos);
	}


}
