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
import net.wohlfart.tools.SimpleMath;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

// @formatter:off
/**
 * this class does an extra render pass to create a glow overlay
 *
 * see: http://www.gamasutra.com/view/feature/130520/realtime_glow.php?page=2
 *      http://www.lwjgl.org/wiki/index.php?title=Using_Frame_Buffer_Objects_%28FBO%29
 *      https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson5
 *      http://lwjgl.org/forum/index.php?topic=4269.0;wap2
 */
public class GlowRenderBatch<T extends IsUpdatable> implements RenderBatch<T>, IsUpdatable { // REVIEWED

    private IGraphicContext graphicContext;

    private final HashSet<T> elements = new HashSet<T>();
    private final HashSet<VertexLight> lights = new HashSet<VertexLight>(10);

    private final Matrix4f screenProjMatrix = new Matrix4f(); // view and ratio

    private int framebufferID;

    private int depthRenderBufferID;

    private int colorTextureID;

    private int width = 512;
    private int height = 512;

    public void setGraphicContext(IGraphicContext graphicContext) {
        this.graphicContext = graphicContext;
    }

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
        screenProjMatrix.load(new PerspectiveProjectionFab().create(CONTEXT_HOLDER.getSettings()));
        if (graphicContext == null) { // fallback
            graphicContext = new DefaultGraphicContext(ShaderRegistry.DEFAULT_SHADER);
        }
        graphicContext.setup();
        fboSetup();

    }

    private void fboSetup() {

        Settings settings = CONTEXT_HOLDER.getSettings();

        width = settings.getWidth()/2;
        height = settings.getHeight()/2;


        // create a new framebuffer
        framebufferID = EXTFramebufferObject.glGenFramebuffersEXT();
        // and a new depthbuffer
        depthRenderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
        // and a new texture used as a color buffer
        colorTextureID = GL11.glGenTextures();

        // switch to the new framebuffer
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID);

        // initialize color texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);               // make it linear filterd
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer

        // initialize depth renderbuffer
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID);                // bind the depth renderbuffer
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height); // get the data space for it
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer

        checkFBO(framebufferID);

        // Switch back to normal framebuffer rendering
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);

    }



    @Override
    public void render() {

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        // switch to rendering on our FBO
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID);
        // Clear Screen And Depth Buffer on the fbo to red
        GL11.glClearColor (0.5f, 0.2f, 0.2f, 0.5f);
        GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // normal rendering on the FBO
        renderPass1();

        // back to screen rendering
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
        renderPass1();

        TexturedQuad quad1 = new TexturedQuad();
        quad1.setPosition(new Vector3f(0,0,-10));
        quad1.setTexHandle(colorTextureID);
        quad1.render();

        TexturedQuad quad2 = new TexturedQuad();
        quad2.setPosition(new Vector3f(0,5,-10));
        quad2.setTexHandle(colorTextureID);
        quad2.render();

        TexturedQuad quad3 = new TexturedQuad();
        quad3.setPosition(new Vector3f(0,-5,-10));
        quad3.setTexHandle(colorTextureID);
        quad3.render();
    }


    private void renderPass1() {
        assert graphicContext != null : "the graphicContext is null";
        assert graphicContext.isInitialized() : "the graphicContext is not initialized, call setup() before using the graphicContext";

        CONTEXT_HOLDER.setCurrentGraphicContext(graphicContext);
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
