package net.wohlfart.widgets;

import org.lwjgl.opengl.GL11;

public class Renderer {


	public void fillRect(int x, int y, int w, int h) {

		GL11.glPushMatrix();
		
		GL11.glTranslatef(-40, -40, -200);
		GL11.glColor3f(0.1f, 0.4f, 0.9f);
		GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, 50.0f);

		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3f(1.5f,1.5f,1.0f);
		GL11.glVertex3f(x, y, 0);
		GL11.glVertex3f(x+w, y, 0);
		GL11.glVertex3f(x+w, y+h, 0);
		GL11.glVertex3f(x, y+h, 0);
		GL11.glEnd();

		GL11.glPopMatrix();

	}
}
