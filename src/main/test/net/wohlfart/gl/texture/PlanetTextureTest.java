package net.wohlfart.gl.texture;

import static net.wohlfart.Assert.assertEqualVec;
import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.lwjgl.util.vector.Vector3f;


public class PlanetTextureTest {

	CelestialTexture planetTexture;
	int height;
	int width;

	@Before
	public void runBeforeEveryTest() {
		// use a big enough radius so we don't have rounding errors...
		planetTexture = new CelestialTexture(200f, CelestialType.values()[0], 1l); // radius and random planet type
		//Image image = planetTexture.getImage();
		height = planetTexture.getWidth();
		width = planetTexture.getHeight();
	}

	@After
	public void runAfterEveryTest() {
		planetTexture = null;
	}

	@Test
	public void size() {
		//Image image = planetTexture.getImage();
		assertEquals(1257, planetTexture.getHeight());
		assertEquals(1257, planetTexture.getWidth());
	}

	@Test
	public void poleVectors() {
		org.lwjgl.util.vector.Vector3f vec;

		vec = planetTexture.getNormalVector(0, 0);
		assertEqualVec(new Vector3f(0, +1, 0), vec);

		vec = planetTexture.getNormalVector(width - 1, 0);
		assertEqualVec(new Vector3f(0, +1, 0), vec);

		vec = planetTexture.getNormalVector((width - 1) / 2, 0);
		assertEqualVec(new Vector3f(0, +1, 0), vec);

		vec = planetTexture.getNormalVector(0, height - 1);
		assertEqualVec(new Vector3f(0, -1, 0), vec);

		vec = planetTexture.getNormalVector(width - 1, height - 1);
		assertEqualVec(new Vector3f(0, -1, 0), vec);

		vec = planetTexture.getNormalVector((width - 1) / 2, height - 1);
		assertEqualVec(new Vector3f(0, -1, 0), vec);
	}

	@Test
	public void equatorVectors() {
		Vector3f vec;

		vec = planetTexture.getNormalVector(0, (height - 1) / 2);
		assertEqualVec(new Vector3f(0, 0, +1), vec);

		vec = planetTexture.getNormalVector((width - 1) / 4, (height - 1) / 2);
		assertEqualVec(new Vector3f(+1, 0, 0), vec);

		vec = planetTexture.getNormalVector((width - 1) / 2, (height - 1) / 2);
		assertEqualVec(new Vector3f(0, 0, -1), vec);

		vec = planetTexture.getNormalVector((int) (3f * ((float) width - 1) / 4f), (height - 1) / 2);
		assertEqualVec(new Vector3f(-1, 0, 0), vec);
	}

}
