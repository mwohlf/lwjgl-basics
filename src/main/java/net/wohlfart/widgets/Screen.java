package net.wohlfart.widgets;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 * top level element for the 2D UI stuff
 */
public class Screen {

    Collection<AbstractWidget> components = new HashSet<AbstractWidget>();

    public Screen() {
        // components.add(new PanelWidget(-1,-1,100,100));
    }

    public void paint(Renderer renderer) {
        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        GL11.glLoadIdentity(); // reset the view matrix

        final Iterator<AbstractWidget> iter = components.iterator();
        while (iter.hasNext()) {
            iter.next().paint(renderer);
        }
    }

}
