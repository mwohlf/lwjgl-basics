package net.wohlfart.gl.elements;

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
