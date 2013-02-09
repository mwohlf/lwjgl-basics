package net.wohlfart.gl.elements.skybox;

public interface SkyboxParameters {

	interface PerlinNoiseParameters {

		int getOctaves();

		float getPersistence();

		float getFrequency();

		float getW();

	}

	int getSize();

	PerlinNoiseParameters getNoiseParamClouds();

	PerlinNoiseParameters getNoiseParamStars();

}
