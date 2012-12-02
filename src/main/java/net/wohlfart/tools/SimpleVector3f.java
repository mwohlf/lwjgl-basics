package net.wohlfart.tools;

import org.lwjgl.util.vector.Vector3f;

@SuppressWarnings("serial")
public class SimpleVector3f extends Vector3f {
	
	
	public String toString() {
		return "(" + x +"," + y + "," + z +") size: " + Math.sqrt(x * x + y * y + z * z);
	}

}
