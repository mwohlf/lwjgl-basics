package net.wohlfart.gl.texture;

import java.util.HashMap;

public class SkyboxTextures {

	enum Side {
		PLUS_Y,
		MINS_Y,
		PLUS_X,
		MINUS_X,
		PLUS_Z,
		MINUS_Z;
	}

	HashMap<Side, ITexture> sides = new HashMap<>(6);

	public void init() {

	}



}
