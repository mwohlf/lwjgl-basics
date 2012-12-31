package net.wohlfart.basic.states;

import java.util.HashSet;
import java.util.Set;

import net.wohlfart.basic.Game;
import net.wohlfart.gl.CanMoveImpl;
import net.wohlfart.gl.CanRotateImpl;
import net.wohlfart.gl.elements.Arrow;
import net.wohlfart.gl.elements.Circle;
import net.wohlfart.gl.elements.Cube;
import net.wohlfart.gl.elements.Icosphere;
import net.wohlfart.gl.elements.Renderable;
import net.wohlfart.gl.elements.Tetrahedron;
import net.wohlfart.gl.input.InputSource;
import net.wohlfart.gl.input.KeyPressedEvent;
import net.wohlfart.gl.renderer.DefaultRenderer;
import net.wohlfart.gl.renderer.Renderer;
import net.wohlfart.gl.shader.UniformHandle;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMatrix4f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

public class SimpleState implements GameState {

	private Renderer renderer;
	private boolean quit = false;

	private final CanMoveImpl canMove = new CanMoveImpl();
	private final CanRotateImpl canRotate = new CanRotateImpl();
	private final Avatar avatar = new Avatar(canRotate, canMove);

	private final Set<Renderable> renderables = new HashSet<Renderable>();
	//private TrueTypeFont trueTypeFont;

	// package private
	SimpleState() {}


	@Override
	public void setup(final Game game) {

		avatar.setInputSource(InputSource.INSTANCE);

		renderer = new DefaultRenderer();
		renderer.setup();
		renderer.set(UniformHandle.CAM_TO_CLIP, game.getProjectionMatrix());


		renderables.add(new Arrow(new Vector3f(1,0,0), ReadableColor.RED));
		renderables.add(new Arrow(new Vector3f(0,1,0), ReadableColor.GREEN));
		renderables.add(new Arrow(new Vector3f(0,0,1), ReadableColor.BLUE));


		renderables.add(new Circle(1, new Vector3f(0,1,0)));
		renderables.add(new Cube(1));
		renderables.add(new Tetrahedron(3));
		renderables.add(new Icosphere(1));





		// drawables.add(new Quad(shader));
		// drawables.add(new Triangle(shader));

		InputSource.INSTANCE.register(new KeyPressedEvent.Listener(){
			@Override
			public void keyEvent(KeyPressedEvent evt) {
				if (Keyboard.KEY_ESCAPE == evt.getKey()) {
					quit();
				}
			}
		});

		//Font awtFont = new Font("Times New Roman", Font.PLAIN, 24);
		//trueTypeFont = new TrueTypeFont(awtFont, false);
	}


	public synchronized void quit() {
		quit = true;
	}


	@Override
	public void update(float tpf) {

		// rotate the view
		Matrix4f viewMatrix = SimpleMatrix4f.create(canRotate);
		renderer.set(UniformHandle.WORLD_TO_CAM, viewMatrix);

		// move the object
		Matrix4f modelMatrix = SimpleMatrix4f.create(canMove);
		renderer.set(UniformHandle.MODEL_TO_WORLD, modelMatrix);


		/*
		Matrix4f result = new Matrix4f();
		Matrix4f.mul(modelMatrix, viewMatrix, result);
		renderer.set(MatrixHandle.MODEL_TO_WORLD, result);
		*/

	}


	@Override
	public void render() {
		for (Renderable renderable : renderables) {
			renderable.render(renderer);
		}
	}


	@Override
	public boolean isDone() {
		return Display.isCloseRequested() || quit;
	}



	@Override
	public void dispose(Game game) {
		renderer.dispose();
	}

}
