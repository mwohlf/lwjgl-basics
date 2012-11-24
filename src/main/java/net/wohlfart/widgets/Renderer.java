package net.wohlfart.widgets;

import org.lwjgl.opengl.GL11;

public class Renderer {


	public void fillRect(int x, int y, int width, int height) {
		GL11.glPushMatrix();
		//GL11.glEnable(GL11.GL_BLEND);
		//GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		//System.out.println("rendering: " + x + "x" + y + "-" +width + "x" +height);
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(0.5f,0.5f,1.0f);
		GL11.glVertex2f(x, y);
		GL11.glVertex2f(x+width, y);
		GL11.glVertex2f(x+width, y+height);
		GL11.glVertex2f(x, y+height);
		GL11.glEnd();


		GL11.glBegin(GL11.GL_TRIANGLES);
		GL11.glColor3f(0.5f,0.5f,0.5f);
		GL11.glVertex3f(0, 0, 0);
		GL11.glVertex3f(0, 1, 0);
		GL11.glVertex3f(-0.1f, 0, 0);
		GL11.glVertex3f(-0.3f, 1.1f, 0);
		GL11.glVertex3f(-0.9f, 1.2f, 0);
		GL11.glVertex3f(-0.6f, 0.1f, 0);
		GL11.glVertex3f(-1.2f, 0.3f, 0);
		GL11.glVertex3f(-1.5f, 1.1f, 0);
		GL11.glVertex3f(-1.3f, 0.1f, 0);
		GL11.glEnd();

		//GL11.glDisable(GL11.GL_BLEND);
		GL11.glPopMatrix();
	}
}
