package net.wohlfart.basic.states;

import java.awt.Color;
import java.awt.Font;
import java.nio.FloatBuffer;
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

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Matrix4f;

public class SimpleState implements IGameState {

	private int vertexCount;
	private int vaoId;
	private int vboId;
	private SimpleShader shader;
	private TrueTypeFont trueTypeFont;
	
	private boolean quit = false;

	private CanMoveImpl canMove = new CanMoveImpl();
	private CanRotateImpl canRotate = new CanRotateImpl();
	private final Avatar avatar = new Avatar(canRotate, canMove);

	private Set<IDrawable> drawables = new HashSet<IDrawable>();

	// package private
	SimpleState() {}


	@Override
	public void setup(final Game game, final Matrix4f projectionMatrix) {

		Font font = TrueTypeFont.getFont("Courier New", Font.BOLD, 32);
		if (font == null) {
			font = new Font("Serif", Font.BOLD, 32);
		}
		trueTypeFont = new TrueTypeFont(font, true);

		avatar.setInputSource(InputSource.INSTANCE);

		shader = new SimpleShader();
		shader.init();
		shader.setProjectionMatrix(projectionMatrix);
		shader.setViewMatrix(new Matrix4f());
		shader.setModelMatrix(new Matrix4f());

		// drawables.add(Quad.create());
		drawables.add(Triangle.create());
		
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
