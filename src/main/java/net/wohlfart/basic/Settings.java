package net.wohlfart.basic;

import org.lwjgl.opengl.DisplayMode;

public class Settings {

	protected String title = "lwjgl";

	protected int width = 800;
	protected int height = 600;

	protected int sync = 100;


	protected float fieldOfView = 60f;
	protected float nearPlane = 0.1f;
	protected float farPlane = 100f;

	/**
	 * @return display width
	 */
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * @return display height
	 */
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * @return field of view in degree
	 */
	public float getFieldOfView() {
		return fieldOfView;
	}
	public void setFieldOfView(float fieldOfView) {
		this.fieldOfView = fieldOfView;
	}


	/**
	 * @return near pane of the frustum
	 */
	public float getNearPlane() {
		return nearPlane;
	}
	public void setNearPlane(float nearPlane) {
		this.nearPlane = nearPlane;
	}

	/**
	 * @return far pane of the frustum
	 */
	public float getFarPlane() {
		return farPlane;
	}
	public void setFarPlane(float farPlane) {
		this.farPlane = farPlane;
	}


	public String getTitle() {
		return title;
	}
	public int getSync() {
		return sync;
	}

}
