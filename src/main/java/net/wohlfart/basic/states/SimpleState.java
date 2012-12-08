package net.wohlfart.basic.states;

import java.awt.Font;
import java.util.HashSet;
import java.util.Set;

import net.wohlfart.basic.Game;
import net.wohlfart.gl.CanMoveImpl;
import net.wohlfart.gl.CanRotateImpl;
import net.wohlfart.gl.elements.IDrawable;
import net.wohlfart.gl.elements.Quad;
import net.wohlfart.gl.elements.Triangle;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.input.KeyPressedEvent;
import net.wohlfart.gl.shader.SimpleShader;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMatrix4f;
import net.wohlfart.tools.TrueTypeFont;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;

public class SimpleState implements IGameState {

	private SimpleShader shader;
	private boolean quit = false;

	private CanMoveImpl canMove = new CanMoveImpl();
	private CanRotateImpl canRotate = new CanRotateImpl();
	private final Avatar avatar = new Avatar(canRotate, canMove);

	private Set<IDrawable> drawables = new HashSet<IDrawable>();

	// package private
	SimpleState() {}


	@Override
	public void setup(final Game game, final Matrix4f projectionMatrix) {

		avatar.setInputSource(InputSource.INSTANCE);

		shader = new SimpleShader();
		shader.init();
		shader.setProjectionMatrix(projectionMatrix);
		shader.setViewMatrix(new Matrix4f());
		shader.setModelMatrix(new Matrix4f());
		//shader.bind();

		drawables.add(Quad.create(shader));
		//drawables.add(Triangle.create(shader));

		//shader.unbind();

		InputSource.INSTANCE.register(new KeyPressedEvent.Listener(){
			@Override
			public void keyEvent(KeyPressedEvent evt) {
				if (Keyboard.KEY_ESCAPE == evt.getKey()) {
					quit = true;
				}
			}
		});

	}


	@Override
	public void update(float tpf) {

		// rotate the view
		Matrix4f viewMatrix = SimpleMatrix4f.create(canRotate);
		shader.setViewMatrix(viewMatrix);

		// move the object
		Matrix4f modelMatrix = SimpleMatrix4f.create(canMove);
		shader.setModelMatrix(modelMatrix);
	}


	@Override
	public void render() {

		shader.bind();

		for (IDrawable drawable : drawables) {
			drawable.draw();
		}

		//trueTypeFont.drawString(20.0f, 20.0f, "Slick displaying True Type Fonts", 0.5f, 0.5f, TrueTypeFont.ALIGN_CENTER);

		shader.unbind();
	}


	@Override
	public boolean isDone() {
		return Display.isCloseRequested() || quit;
	}



	@Override
	public void teardown(Game game) {
		// nothing to do yet
	}

}
