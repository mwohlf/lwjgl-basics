package net.wohlfart.basic;

import org.lwjgl.opengl.DisplayMode;

public class Settings {
	static Settings DEFAULT = new Settings();

	protected int width = 800;
	protected int height = 600;


	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public DisplayMode getDisplayMode() {
		return new DisplayMode(width, height);
	}
	public boolean getResizable() {
		return true;
	}

}
