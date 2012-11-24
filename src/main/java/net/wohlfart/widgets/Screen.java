package net.wohlfart.widgets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.lwjgl.opengl.GL11;




public class Screen {

	Collection<AbstractWidget> components = new HashSet<AbstractWidget>();
	
	
	public Screen() {
		components.add(new PanelWidget(10,10,110,110));
	}

	public void paint(Renderer renderer) {
		GL11.glLoadIdentity();
		
		Iterator<AbstractWidget> iter = components.iterator();
		while (iter.hasNext()) {
			iter.next().paint(renderer);
		}
	}

}
