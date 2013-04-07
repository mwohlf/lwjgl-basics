package net.wohlfart.gl.elements.skybox;

/**
 * <p>SkyboxParameters interface.</p>
 */
public interface SkyboxParameters {

    interface PerlinNoiseParameters {

        int getOctaves();

        float getPersistence();

        float getFrequency();

        float getW();

    }

    /**
     * <p>getNoiseParamClouds.</p>
     *
     * @return a {@link net.wohlfart.gl.elements.skybox.SkyboxParameters.PerlinNoiseParameters} object.
     */
    PerlinNoiseParameters getNoiseParamClouds();

    /**
     * <p>getNoiseParamStars.</p>
     *
     * @return a {@link net.wohlfart.gl.elements.skybox.SkyboxParameters.PerlinNoiseParameters} object.
     */
    PerlinNoiseParameters getNoiseParamStars();

}
