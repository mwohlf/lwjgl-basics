package net.wohlfart.basic;

/**
 * <p>Settings class.</p>
 */
public class Settings {

    protected String title = "lwjgl";

    protected int width = 800;
    protected int height = 600;

    protected int sync = 100;

    protected float fieldOfView = 60f;
    protected float nearPlane = 0.1f;
    protected float farPlane = 100f;

    private boolean fullscreen = false;

    /**
     * <p>Getter for the field <code>width</code>.</p>
     *
     * @return display width
     */
    public int getWidth() {
        return width;
    }

    /**
     * <p>Setter for the field <code>width</code>.</p>
     *
     * @param width a int.
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * <p>Getter for the field <code>height</code>.</p>
     *
     * @return display height
     */
    public int getHeight() {
        return height;
    }

    /**
     * <p>Setter for the field <code>height</code>.</p>
     *
     * @param height a int.
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * <p>Getter for the field <code>fieldOfView</code>.</p>
     *
     * @return field of view in degree
     */
    public float getFieldOfView() {
        return fieldOfView;
    }

    /**
     * <p>Setter for the field <code>fieldOfView</code>.</p>
     *
     * @param fieldOfView a float.
     */
    public void setFieldOfView(float fieldOfView) {
        this.fieldOfView = fieldOfView;
    }

    /**
     * <p>Getter for the field <code>nearPlane</code>.</p>
     *
     * @return near pane of the frustum
     */
    public float getNearPlane() {
        return nearPlane;
    }

    /**
     * <p>Setter for the field <code>nearPlane</code>.</p>
     *
     * @param nearPlane a float.
     */
    public void setNearPlane(float nearPlane) {
        this.nearPlane = nearPlane;
    }

    /**
     * <p>Getter for the field <code>fullscreen</code>.</p>
     *
     * @return treu if the application runs in fullscreen mode
     */
    public boolean getFullscreen() {
        return fullscreen;
    }

    /**
     * <p>Setter for the field <code>fullscreen</code>.</p>
     *
     * @param fullscreen a boolean.
     */
    public void setFullscreen(boolean fullscreen) {
        this.fullscreen = fullscreen;
    }

    /**
     * <p>Getter for the field <code>farPlane</code>.</p>
     *
     * @return far pane of the frustum
     */
    public float getFarPlane() {
        return farPlane;
    }

    /**
     * <p>Setter for the field <code>farPlane</code>.</p>
     *
     * @param farPlane a float.
     */
    public void setFarPlane(float farPlane) {
        this.farPlane = farPlane;
    }

    /**
     * <p>Getter for the field <code>title</code>.</p>
     *
     * @return a {@link java.lang.String} object.
     */
    public String getTitle() {
        return title;
    }

    /**
     * <p>Getter for the field <code>sync</code>.</p>
     *
     * @return a int.
     */
    public int getSync() {
        return sync;
    }

}
