package net.wohlfart.gl.texture;

import java.awt.Color;

import net.wohlfart.tools.ColorGradient;
import net.wohlfart.tools.SimplexNoise;

/**
 * different strategies for building planets
 */
public enum CelestialType {

    GREEN {
        {
            maxRadius = 2f;
            minRadius = 0.5f;
        }

        @Override
        Color getColor(final float x, final float y, final float z, final float v) {
            return Color.GREEN.brighter();
        }
    },

    LAVA_PLANET {
        {
            maxRadius = 4f;
            minRadius = 1f;
        }
        ColorGradient gradient = new ColorGradient(Color.RED.brighter(), Color.YELLOW);

        @Override
        Color getColor(final float x, final float y, final float z, final float v) {
            final double noise = createNoise(x, y, z, v, 0.5f, 5);
            return gradient.getColor(noise);
        }
    },

    WATER_PLANET {
        {
            maxRadius = 5f;
            minRadius = 1f;
        }
        ColorGradient gradient = new ColorGradient(Color.BLUE, Color.WHITE);

        @Override
        Color getColor(final float x, final float y, final float z, final float v) {
            final double noise = createNoise(x, y, z, v, 0.5f, 5);
            return gradient.getColor(noise);
        }
    },

    GAS_PLANET {
        {
            maxRadius = 7f;
            minRadius = 1f;
        }
        ColorGradient gradient = new ColorGradient(new Color(255, 213, 133), new Color(102, 68, 58));

        @Override
        Color getColor(final float x, final float y, final float z, final float v) {
            final double noise = createNoise(x / 1.5f, y * 10, z / 1.5f, v, 0.5f, 5);
            return gradient.getColor(noise);
        }
    },

    CONTINENTAL_PLANET {
        {
            maxRadius = 6f;
            minRadius = 3f;
        }
        ColorGradient gradient = new ColorGradient(new Color(0, 0, 0), new Color(0, 0, 100), new Color(0, 0, 255), new Color(10, 10, 255), new Color(180, 180,
                180), new Color(10, 255, 10), new Color(0, 255, 0), new Color(0, 50, 0));

        @Override
        Color getColor(final float x, final float y, final float z, final float v) {
            final double groundNoise = createNoise(x, (float) Math.asin(y), z, v, 0.5f, 4);
            final Color ground = gradient.getColor(groundNoise);
            final double skyNoise = createNoise(x * 2, (float) Math.asin(y) * 4, z * 2, v, 0.2f, 3);
            return ColorGradient.linearGradient(ground, Color.WHITE, skyNoise);
        }
    };

    // all lengths in [10^3 km]

    public float minRadius = 6.371f; // in 10^6 m = 1000 km (earth has 6.371 x10^3 km)
    public float maxRadius = 100.0f; // sun has 696 x10^3 km

    public float maxPathRadius = 6000000.0f;
    public float minPathRadius = 700.0f; // sun-earth is 147098; sun-pluto is 5913520

    public float minRot = (float) Math.PI * 2f / 60f; // in rad/s, 2pi mean one rotation per second 2pi/3 means one rotation in 3 sec
    public float maxRot = (float) Math.PI * 2f / 300f;

    public float maxAxisDeplacement = 0.25f; // this value is randomly added to a normalized up vectors x and y values, earth is around 23.4 degree

    /**
     * fallback
     *
     * @param x
     *            [0..1]
     * @param y
     *            [0..1]
     * @param z
     *            [0..1]
     * @param textureVariant
     * @param texture
     *            , needed for access to some randomness
     * @return the color on the surface at the location with the specified normal vector
     */
    Color getColor(final float x, final float y, final float z, final float textureVariant) {
        return Color.YELLOW;
    }

    // adding octaves
    double createNoise(final float x, final float y, final float z, final float v, final float persistence, final int octaves) {
        double result = 0;
        float max = 0;
        for (int i = 0; i < octaves; i++) {
            final float frequency = (float) Math.pow(2, i);
            final float amplitude = (float) Math.pow(persistence, i);
            result += createNoise(x, y, z, v, amplitude, frequency);
            max += amplitude;
        }
        return result / max;
    }

    // calling the noise
    double createNoise(final float x, final float y, final float z, final float v, final float amplitude, final float frequency) {
        // the noise returns [-1 .. +1]
        // double noise = PerlinNoise.noise(x * frequency, y * frequency, z *
        // frequency);
        final double noise = SimplexNoise.noise(x * frequency, y * frequency, z * frequency, v);
        return amplitude * noise;
    }

}
