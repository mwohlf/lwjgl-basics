package net.wohlfart.gl.spatial;

import net.wohlfart.gl.action.Action.Actor;
import net.wohlfart.gl.renderer.IsRenderable;
import net.wohlfart.gl.renderer.IsUpdateable;
import net.wohlfart.gl.view.CanMove;
import net.wohlfart.gl.view.CanRotate;

public interface Spatial extends IsRenderable, IsUpdateable, CanRotate, CanMove, Actor {


}
