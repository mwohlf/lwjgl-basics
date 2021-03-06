package net.wohlfart.gl.texture;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class PlanetTextureTest {

    CelestialTexture planetTexture;
    int height;
    int width;


    @Before
    public void runBeforeEveryTest() {
        // use a big enough radius so we don't have rounding errors...
        planetTexture = new CelestialTexture(200f, CelestialType.values()[0], 1l, 1); // radius and random planet type
        // Image image = planetTexture.getImage();
        height = planetTexture.getWidth();
        width = planetTexture.getHeight();
    }


    @After
    public void runAfterEveryTest() {
        planetTexture = null;
    }


    @Test
    public void size() {
        // Image image = planetTexture.getImage();
        assertEquals(1257, planetTexture.getHeight());
        assertEquals(1257, planetTexture.getWidth());
    }


}
