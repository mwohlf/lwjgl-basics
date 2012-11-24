package net.wohlfart.tools;

@SuppressWarnings("serial")
public class Vector3f extends org.lwjgl.util.vector.Vector3f {
	
	
	public String toString() {
		return "(" + x +"," + y + "," + z +") size: " + Math.sqrt(x * x + y * y + z * z);
	}

}
