package net.wohlfart.gl.action;

import net.wohlfart.gl.view.CanMove;

import org.lwjgl.util.vector.Vector3f;

public class MoveAction implements Action {

    private final CanMove canMove;
    private final Vector3f movePerSec;


    public MoveAction(CanMove canMove) {
        this(canMove, new Vector3f(0,1,0));
    }

    public MoveAction(CanMove canMove, Vector3f movePerSec) {
        this.canMove = canMove;
        this.movePerSec = movePerSec;
    }

    @Override
    public void update(float timeInSec) {
        Vector3f v = canMove.getPosition();
       // SimpleMath.add(v, movePerSec.scale(1f/timeInSec), v);
        canMove.setPosition(v);
    }

}
