package net.wohlfart.basic.hud.widgets;

import net.wohlfart.gl.shader.GraphicContextHolder;
import net.wohlfart.gl.view.Camera;

import org.lwjgl.util.vector.Vector3f;


public class CamPositionLabel extends FormattedLabel {

    private final Camera cam;
    private final Vector3f vec = new Vector3f();

    public CamPositionLabel(int x, int y) {
        super(x, y, "cam position: {0,number,#0.000},{1,number,#0.000},{2,number,#0.000}");
        cam = GraphicContextHolder.CONTEXT_HOLDER.getCamera();
    }

    @Override
    public void update(float timeInSec) {
        vec.set(cam.getPosition());
        setValue(new Object[] { vec.x, vec.y, vec.z });
        super.update(timeInSec);
    }

}
