package net.wohlfart.basic.container;

import static net.wohlfart.gl.shader.GraphicContextHolder.CONTEXT_HOLDER;
import static net.wohlfart.gl.shader.ShaderUniformHandle.CAM_TO_CLIP;
import static net.wohlfart.gl.shader.ShaderUniformHandle.MODEL_TO_WORLD;
import static net.wohlfart.gl.shader.ShaderUniformHandle.WORLD_TO_CAM;

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
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

// @formatter:off
/**
 * this class does an extra render pass to create a glow overlay
 *
 * see: http://www.gamasutra.com/view/feature/130520/realtime_glow.php?page=2
 *      http://www.lwjgl.org/wiki/index.php?title=Using_Frame_Buffer_Objects_%28FBO%29
 *      https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson5
 *      http://lwjgl.org/forum/index.php?topic=4269.0;wap2
 *      http://www.gamedev.net/topic/295513-glowbloom-effect/
 *      http://wiki.delphigl.com/index.php/Shadersammlung
 */
public class GlowRenderBatch<T extends IsUpdatable> implements RenderBatch<T>, IsUpdatable { // REVIEWED

    private IGraphicContext screenGraphicContext;
    private IGraphicContext fboGraphicContext;
    private IGraphicContext fboBlurGraphicContext;

    private final HashSet<T> elements = new HashSet<T>();
    private final HashSet<VertexLight> lights = new HashSet<VertexLight>(10);

    private final Matrix4f screenProjMatrix = new Matrix4f(); // view and ratio

    private int fboHandleA;
    private int depthRenderBufferA;
    private int colorTextureA;

    private int fboHandleB;
    private int depthRenderBufferB;
    private int colorTextureB;


    private int textureWidth = 512;
    private int textureHeight = 512;

    private int screenWidth = 512;
    private int screenHeight = 512;



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

        textureWidth = screenWidth/4;
        textureHeight = screenHeight/4;

        fboGraphicContext = new DefaultGraphicContext(ShaderRegistry.FBO_SHADER);
        fboGraphicContext.setup();

        fboBlurGraphicContext = new DefaultGraphicContext(ShaderRegistry.FBO_BLUR_SHADER);
        fboBlurGraphicContext.setup();

        fboSetupA();
        fboSetupB();

    }

    private void fboSetupA() {
        // create a new framebuffer
        fboHandleA = EXTFramebufferObject.glGenFramebuffersEXT();
        // and a new depthbuffer
        depthRenderBufferA = EXTFramebufferObject.glGenRenderbuffersEXT();
        // and a new texture used as a color buffer
        colorTextureA = GL11.glGenTextures();

        // switch to the new FBO target can be:  GL_FRAMEBUFFER​, GL_READ_FRAMEBUFFER​, or GL_DRAW_FRAMEBUFFER​
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandleA);

        // initialize color texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureA);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);               // make it linear filterd
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, textureWidth, textureHeight, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTextureA, 0); // attach it to the framebuffer

        // initialize depth renderbuffer
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferA);                // bind the depth renderbuffer
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, textureWidth, textureHeight); // get the data space for it
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferA); // bind it to the renderbuffer

        checkFBO(fboHandleA);

        // Switch back to normal framebuffer rendering
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
    }

    private void fboSetupB() {
        // create a new framebuffer
        fboHandleB = EXTFramebufferObject.glGenFramebuffersEXT();
        // and a new depthbuffer
        depthRenderBufferB = EXTFramebufferObject.glGenRenderbuffersEXT();
        // and a new texture used as a color buffer
        colorTextureB = GL11.glGenTextures();

        // switch to the new FBO target can be:  GL_FRAMEBUFFER​, GL_READ_FRAMEBUFFER​, or GL_DRAW_FRAMEBUFFER​
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandleB);

        // initialize color texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureB);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);               // make it linear filterd
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, textureWidth, textureHeight, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTextureB, 0); // attach it to the framebuffer

        // initialize depth renderbuffer
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferB);                // bind the depth renderbuffer
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, textureWidth, textureHeight); // get the data space for it
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferA); // bind it to the renderbuffer

        checkFBO(fboHandleB);

        // Switch back to normal framebuffer rendering
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
    }

    @Override
    public void render() {

        // normal rendering to the FBO
        renderOffScreen();
        // blur the FBO to another FBO
        blurOffScreen();

        // back to screen rendering
        GL11.glViewport(0, 0, screenWidth, screenHeight);
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
        renderOnScreen();
    }


    private void renderOffScreen() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        // switch to rendering on our FBO
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandleA);
        // clear screen and depth buffer on the FBO
        GL11.glClearColor (1f, 0.2f, 0.2f, 0f);
        GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glViewport(0, 0, textureWidth, textureHeight);

        CONTEXT_HOLDER.setCurrentGraphicContext(fboGraphicContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        ShaderUniformHandle.WORLD_TO_CAM.set(CONTEXT_HOLDER.getCamera().getWorldToCamMatrix());
        ShaderUniformHandle.CAM_TO_CLIP.set(screenProjMatrix);

        int i = 0;
        for (final VertexLight light : lights) {
            ShaderUniformHandle.LIGHTS.set(light, i++);
        }

        for (T element : elements) {
            element.render();
        }
    }

    private void blurOffScreen() {
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        // switch to rendering on our FBO
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandleB);
        // clear screen and depth buffer on the FBO
        GL11.glClearColor (0.2f, 1f, 0.2f, 0f);
        GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        GL11.glViewport(0, 0, textureWidth, textureHeight);

        CONTEXT_HOLDER.setCurrentGraphicContext(fboBlurGraphicContext);
        MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX);
        WORLD_TO_CAM.set(SimpleMath.UNION_MATRIX);
        CAM_TO_CLIP.set(SimpleMath.UNION_MATRIX);

        ShaderUniformHandle.DIRECTION.set(new Vector2f(1,0));
        ShaderUniformHandle.RADIUS.set(2f);

        TexturedQuad quad1 = new TexturedQuad();
        quad1.setPosition(new Vector3f(0,2,-10));
        quad1.setTexHandle(colorTextureA);
        quad1.render();
    }



    private void renderOnScreen() {
        CONTEXT_HOLDER.setCurrentGraphicContext(screenGraphicContext);
        ShaderUniformHandle.MODEL_TO_WORLD.set(SimpleMath.UNION_MATRIX); // the default, each model should set its own later
        ShaderUniformHandle.WORLD_TO_CAM.set(CONTEXT_HOLDER.getCamera().getWorldToCamMatrix());
        ShaderUniformHandle.CAM_TO_CLIP.set(screenProjMatrix);

        int i = 0;
        for (final VertexLight light : lights) {
            ShaderUniformHandle.LIGHTS.set(light, i++);
        }

        for (T element : elements) {
            element.render();
        }

        TexturedQuad quad1 = new TexturedQuad();
        quad1.setPosition(new Vector3f(0,2,-10));
        quad1.setTexHandle(colorTextureA);
        quad1.render();

        TexturedQuad quad2 = new TexturedQuad();
        quad2.setPosition(new Vector3f(0,-2,-10));
        quad2.setTexHandle(colorTextureB);
        quad2.render();
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
