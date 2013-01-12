package net.wohlfart.gl.texture;

import java.awt.Color;

public class SimpleProcTexture extends ProceduralTexture {


	public SimpleProcTexture(int width, int height) {
		super(width, height);
	}

	@Override
	protected int[] initialize(int width, int height, int[] data) {
		for (int x = 0; x < width; x++) {
			setPixel(x, height/2, Color.BLUE, data);
		}
		return data;
	}


}
