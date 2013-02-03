package net.wohlfart.basic.states;

import net.wohlfart.gl.elements.ColoredQuad;
import net.wohlfart.gl.elements.Skybox;
import net.wohlfart.gl.elements.TexturedQuad;
import net.wohlfart.gl.elements.debug.Arrow;
import net.wohlfart.gl.elements.debug.Circle;
import net.wohlfart.gl.elements.debug.CubeMesh;
import net.wohlfart.gl.elements.debug.IcosphereMesh;
import net.wohlfart.gl.elements.debug.TerahedronRefinedMesh;
import net.wohlfart.gl.elements.debug.TetrahedronMesh;
import net.wohlfart.gl.input.CommandEvent;
import net.wohlfart.gl.renderer.RenderBucket;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.DefaultShaderProgram;
import net.wohlfart.gl.shader.GraphicContextManager;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.shader.WireframeShaderProgram;
import net.wohlfart.model.Avatar;
import net.wohlfart.tools.SimpleMath;
import net.wohlfart.tools.SimpleMatrix4f;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.ReadableColor;
import org.lwjgl.util.vector.Quaternion;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.eventbus.Subscribe;

class SimpleState implements GameState {
	protected static final Logger LOGGER = LoggerFactory.getLogger(SimpleState.class);

	private GraphicContextManager.IGraphicContext defaultGraphicContext;
	private GraphicContextManager.IGraphicContext wireframeGraphicContext;

	private final RenderBucket skyboxBucket = new RenderBucket();
	private final RenderBucket elemBucket = new RenderBucket();
	private final RenderBucket uiBucket = new RenderBucket();

	private boolean quit = false;

	private final Avatar avatar = new Avatar();



	@Override
	public void setup() {

		avatar.setup();

		// FIXME: we dont need to set them both here
		wireframeGraphicContext = new DefaultGraphicContext(new WireframeShaderProgram());
		GraphicContextManager.INSTANCE.setCurrentGraphicContext(wireframeGraphicContext);

		defaultGraphicContext = new DefaultGraphicContext(new DefaultShaderProgram());
		GraphicContextManager.INSTANCE.setCurrentGraphicContext(defaultGraphicContext);

		// render the skybox first
		skyboxBucket.add(new Skybox());

		elemBucket.add(new Arrow(new Vector3f(1, 0, 0))
				.color(ReadableColor.RED));
		elemBucket.add(new Arrow(new Vector3f(0, 1, 0))
				.color(ReadableColor.GREEN));
		elemBucket.add(new Arrow(new Vector3f(0, 0, 1))
				.color(ReadableColor.BLUE));

		elemBucket.add(new TerahedronRefinedMesh(2, 1).lineWidth(1)
				.color(ReadableColor.RED).translate(new Vector3f(3, 5, 0)));
		elemBucket.add(new TerahedronRefinedMesh(2, 2).lineWidth(2)
				.color(ReadableColor.GREEN).translate(new Vector3f(0, 5, 0)));
		elemBucket.add(new TerahedronRefinedMesh(2, 1).lineWidth(2)
				.color(ReadableColor.BLUE).translate(new Vector3f(-3, 5, 0)));

		elemBucket.add(new TetrahedronMesh(3).lineWidth(2)
				.color(ReadableColor.WHITE).translate(new Vector3f(-3, -5, 0)));

		elemBucket.add(new CubeMesh(1)
				.lineWidth(1)
				.color(ReadableColor.ORANGE)
				.translate(new Vector3f(-3, -2, 0))
				.rotate(SimpleMath.createQuaternion(new Vector3f(1, 0, -1),
						new Vector3f(0, 1, 0), new Quaternion())));

		elemBucket.add(new Circle(1).lineWidth(2).translate(
				new Vector3f(3, 2, 0)));

		elemBucket.add(new IcosphereMesh(1, 1).lineWidth(1)
				.color(ReadableColor.RED).translate(new Vector3f(5, -7, 0)));
		elemBucket.add(new IcosphereMesh(1, 2).lineWidth(2)
				.color(ReadableColor.GREEN).translate(new Vector3f(0, -7, 0)));
		elemBucket.add(new IcosphereMesh(1, 1).lineWidth(2)
				.color(ReadableColor.BLUE).translate(new Vector3f(-5, -7, 0)));

		elemBucket.add(new TexturedQuad().translate(new Vector3f(-1, 5, 0)));

		elemBucket.add(new ColoredQuad().translate(new Vector3f(-1, 5, 0)));

		GraphicContextManager.INSTANCE.getInputDispatcher().register(this);

		// Font awtFont = new Font("Times New Roman", Font.PLAIN, 24);
		// trueTypeFont = new TrueTypeFont(awtFont, false);
	}

	@Subscribe
	public synchronized void onExitTriggered(CommandEvent.Exit exitEvent) {
		quit = true;
	}

	@Override
	public void update(float tpf) {
		LOGGER.debug("update called with tpf/fps {}/{}", tpf, 1f/tpf);
		// todo:
		//   poll the user input
		//   move the models
		//   calculate the world translation and rotation
	}



	@Override
	public void render() {

		GraphicContextManager.INSTANCE.setCurrentGraphicContext(defaultGraphicContext);
		ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX); // no move
		ShaderUniformHandle.WORLD_TO_CAM.set(SimpleMatrix4f.create(avatar.getRotation()));
		ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextManager.INSTANCE.getProjectionMatrix());
		skyboxBucket.render();

		GraphicContextManager.INSTANCE.setCurrentGraphicContext(wireframeGraphicContext);
		ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMatrix4f.create(avatar.getPosition()));
		ShaderUniformHandle.WORLD_TO_CAM.set(SimpleMatrix4f.create(avatar.getRotation()));
		ShaderUniformHandle.CAM_TO_CLIP.set(GraphicContextManager.INSTANCE.getProjectionMatrix());
		elemBucket.render();

		uiBucket.render();
	}

	@Override
	public boolean isDone() {
		return Display.isCloseRequested() || quit;
	}

	@Override
	public void dispose() {
		defaultGraphicContext.dispose();
		wireframeGraphicContext.dispose();
		avatar.dispose();
	}

}
