package net.wohlfart.basic.container;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;

import java.util.Collection;
import java.util.HashSet;

import net.wohlfart.basic.Settings;
import net.wohlfart.basic.elements.IsUpdatable;
import net.wohlfart.gl.elements.TexturedQuad;
import net.wohlfart.gl.shader.DefaultGraphicContext;
import net.wohlfart.gl.shader.GraphicContextHolder.IGraphicContext;
import net.wohlfart.gl.shader.PerspectiveProjectionFab;
import net.wohlfart.gl.shader.ShaderRegistry;
import net.wohlfart.gl.shader.ShaderUniformHandle;
import net.wohlfart.gl.shader.VertexLight;
import net.wohlfart.gl.texture.FrameBufferObject;
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

// @formatter:off
/**
 * this class does an extra render pass to create a glow overlay
 *
 * see: https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson5
 *      http://www.gamasutra.com/view/feature/130520/realtime_glow.php?page=2
 *      http://www.lwjgl.org/wiki/index.php?title=Using_Frame_Buffer_Objects_%28FBO%29
 *      http://lwjgl.org/forum/index.php?topic=4269.0;wap2
 *      http://www.gamedev.net/topic/295513-glowbloom-effect/
 *      http://wiki.delphigl.com/index.php/Shadersammlung
 */
public class GlowRenderBatch<T extends IsUpdatable> implements RenderBatch<T>, IsUpdatable { // REVIEWED

    private static final float BLUR_DIST = 1f;
    private static final int SCALE_DOWN = 1;

    private IGraphicContext screenGraphicContext;
    private IGraphicContext fboGraphicContext;
    private IGraphicContext fboBlurGraphicContext;

    private final HashSet<T> elements = new HashSet<T>();
    private final HashSet<VertexLight> lights = new HashSet<VertexLight>(10);

    private final Matrix4f screenProjMatrix = new Matrix4f(); // view and ratio

    private int textureWidth;
    private int textureHeight;

    private int screenWidth;
    private int screenHeight;


    private DefaultGraphicContext plainContext;


    private FrameBufferObject frameBuffer1;
    private FrameBufferObject frameBuffer2;



    public void add(VertexLight light) {
        lights.add(light);
    }

    public void add(T element) {
        elements.add(element);
    }

    public void addAll(Collection<T> multipleElements) {
        elements.addAll(multipleElements);
    }

    public HashSet<T> getElements() {
        return elements;
    }

    @Override
    public void setup() {
        Settings settings = CONTEXT_HOLDER.getSettings();
        screenProjMatrix.load(new PerspectiveProjectionFab().create(settings));
        screenGraphicContext = new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER);
        screenGraphicContext.setup();

        screenWidth = settings.getWidth();
        screenHeight = settings.getHeight();

        textureWidth = screenWidth/SCALE_DOWN;
        textureHeight = screenHeight/SCALE_DOWN;

        fboGraphicContext = new DefaultGraphicContext(ShaderRegistry.FBO_STD_SHADER);
        fboGraphicContext.setup();

        fboBlurGraphicContext = new DefaultGraphicContext(ShaderRegistry.FBO_BLUR_SHADER);
        fboBlurGraphicContext.setup();

        plainContext = new DefaultGraphicContext(ShaderRegistry.PLAIN_SHADER);
        plainContext.setup();


        frameBuffer1 = new FrameBufferObject(textureWidth, textureHeight);
        frameBuffer1.setup();

        frameBuffer2 = new FrameBufferObject(textureWidth, textureHeight);
        frameBuffer2.setup();
    }

    @Override
    public void render() {
        CONTEXT_HOLDER.setCurrentGraphicContext(fboGraphicContext);
        render2Buffer(frameBuffer1);

        CONTEXT_HOLDER.setCurrentGraphicContext(fboBlurGraphicContext);
        ShaderUniformHandle.DIRECTION.set(ShaderUniformHandle.HORIZONTAL);
        ShaderUniformHandle.DISTANCE.set(BLUR_DIST/textureWidth);
        blurBuffer(frameBuffer1, frameBuffer2);

        CONTEXT_HOLDER.setCurrentGraphicContext(fboBlurGraphicContext);
        ShaderUniformHandle.DIRECTION.set(ShaderUniformHandle.VERTICAL);
        ShaderUniformHandle.DISTANCE.set(BLUR_DIST/textureHeight);
        blurBuffer(frameBuffer2, frameBuffer1);

        CONTEXT_HOLDER.setCurrentGraphicContext(fboBlurGraphicContext);
        ShaderUniformHandle.DIRECTION.set(ShaderUniformHandle.VERTICAL);
        ShaderUniformHandle.DISTANCE.set(0);
        render2Screen(frameBuffer1);
    }

    private void render2Buffer(FrameBufferObject target) {
        target.bind();
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(CONTEXT_HOLDER.getCamera().getWorldToCamMatrix());
        ShaderUniformHandle.CAM_TO_CLIP.set(screenProjMatrix);
        for (T element : elements) {
            element.render();
        }
        target.unbind();
    }

    private void blurBuffer(FrameBufferObject source, FrameBufferObject target) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        // switch to rendering on our FBO, this is the render target now
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, target.getTexture());
        // clear screen and depth buffer on the FBO
        GL11.glClearColor (0f, 0f, 0f, 0f);
        GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        // normal rendering to the FBO
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA); // pre-multiplied alpha


        GL11.glViewport(0, 0, textureWidth, textureHeight);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.CAM_TO_CLIP.set(SimpleMath.UNION_MATRIX);

        // FIXME: we don't need a 3D quad if we only want to copy/blur a texture...
        // FIXME: use clamp to border here
        TexturedQuad quad1 = new TexturedQuad();
        quad1.setPosition(new Vector3f(0,0,0));
        quad1.setTexHandle(source.getTexture()); // this is the render source
        quad1.setSize(2); // texture is [0..1] camspace is [-1...+1]
        quad1.render();
    }

    private void render2Screen(FrameBufferObject source) {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        // switch to rendering on the screen
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
        // clear screen and depth buffer on the FBO
        //GL11.glClearColor (1f, 1f, 1f, 0f);
        //GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glViewport(0, 0, screenWidth, screenHeight);

        //CONTEXT_HOLDER.setCurrentGraphicContext(plainContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.CAM_TO_CLIP.set(SimpleMath.UNION_MATRIX);

        // normal rendering to the FBO
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA); // pre-multiplied" alpha

        // FIXME: we don't need a 3D quad if we only want to copy/blur a texture...
        // FIXME: use clamp to border here
        TexturedQuad quad1 = new TexturedQuad();
        quad1.setPosition(new Vector3f(0,0,0));
        //quad1.setPosition(CONTEXT_HOLDER.getCamera().getPosition());
        quad1.setTexHandle(source.getTexture()); // this is the render source
        quad1.setSize(2);
        quad1.render();
    }

    @Override
    public void update(float timeInSec) {
        for (final T element : elements) {
            element.update(timeInSec);
        }
    }

    @Override
    public void destroy() {
        for (final T element : elements) {
            element.destroy();
        }
        elements.clear();
    }

    private void checkFBO(int fboHandle) {
        int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT );
        switch ( framebuffer ) {
        case EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT:
            break;
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_FORMATS_EXT exception" );
        case EXTFramebufferObject.GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT:
            throw new RuntimeException( "FrameBuffer: " + fboHandle
                    + ", has caused a GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER_EXT exception" );
        default:
            throw new RuntimeException( "Unexpected reply from glCheckFramebufferStatusEXT: " + framebuffer );
        }
    }
}
