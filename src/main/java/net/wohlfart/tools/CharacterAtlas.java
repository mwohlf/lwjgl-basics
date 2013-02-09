package net.wohlfart.tools;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class CharacterAtlas {

	HashMap<Character, CharInfo> map = new HashMap<Character, CharInfo>();
	BufferedImage buffImage;

	private class CharInfo {
		char c;
		float x;
		float y;
		float w;
		float h;

		CharInfo(char c, float x, float y, float w, float h) {
			this.c = c;
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
		}
	}

	public void put(char c, float x, float y, float w, float h) {
		map.put(c, new CharInfo(c, x, y, w, h));
	}

	public void setImage(BufferedImage buffImage) {
		this.buffImage = buffImage;
	}

	public BufferedImage getImage() {
		return buffImage;
	}

}
