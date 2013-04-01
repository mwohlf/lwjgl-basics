package net.wohlfart.gl.action;

import net.wohlfart.gl.view.CanRotate;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;

public class RotateAction implements Action {

    private final CanRotate canRotate;
    private final float rotPerSec;
    private final Vector3f axis;


    public RotateAction(CanRotate canRotate) {  // TODO: remove the param from the constructor and add a double dispatch
        this(canRotate, 10f, new Vector3f(0,1,0));
    }

    public RotateAction(CanRotate canRotate, float rotPerSec) {
        this(canRotate, rotPerSec, new Vector3f(0,1,0));
    }

    public RotateAction(CanRotate canRotate, float rotPerSec, Vector3f axis) {
        this.canRotate = canRotate;
        this.rotPerSec = rotPerSec;
        this.axis = axis;
    }

    @Override
    public void update(float timeInSec) {
        Quaternion q = canRotate.getRotation();
        SimpleMath.rotate(q, timeInSec * SimpleMath.TWO_PI / rotPerSec, axis);
        canRotate.setRotation(q);
    }

}
