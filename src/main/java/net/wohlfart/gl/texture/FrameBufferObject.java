package net.wohlfart.gl.texture;

import net.wohlfart.gl.shader.ShaderUniformHandle;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;


// FIXME: checkout
// http://gamedev.stackexchange.com/questions/19804/how-can-i-downsample-a-texture-using-fbos
// for info about mipmaps

public class FrameBufferObject {
    private static final float BLUR_DIST = 1f;

    private final int width;
    private final int height;
    private final int filterMapping;  // GL_NEAREST, GL_LINEAR without mipmaps


    private int fboHandle;
    private int depthRenderBuffer;
    private int colorTexture;

    public FrameBufferObject(int width, int height) {
        this.width = width;
        this.height = height;
        this.filterMapping = GL11.GL_NEAREST;
    }


    public void setup() {

        // create a new framebuffer
        fboHandle = EXTFramebufferObject.glGenFramebuffersEXT();
        // and a new depthbuffer
        depthRenderBuffer = EXTFramebufferObject.glGenRenderbuffersEXT();
        // and a new texture used as a color buffer
        colorTexture = GL11.glGenTextures();

        // switch to the new FBO target can be:  GL_FRAMEBUFFER​, GL_READ_FRAMEBUFFER​, or GL_DRAW_FRAMEBUFFER​
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandle);

        // initialize color texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL12.GL_TEXTURE_MAX_LEVEL, 20);
        //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_TRUE);

        // we want GL11.GL_NEAREST so we don'tmix in the background color of the first texture/fbo at the edges
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, filterMapping); // GL_NEAREST, GL_LINEAR without mipmaps
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, filterMapping);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,
                EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTexture, 0); // attach it to the framebuffer

        // initialize depth renderbuffer
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBuffer);                // bind the depth renderbuffer
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height); // get the data space for it
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBuffer); // bind it to the renderbuffer

        checkFBO(fboHandle);
    }

    public void bind() {

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        // switch to rendering on our FBO, this is the render target now
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandle);
        // clear screen and depth buffer on the FBO
        //GL11.glClearColor (0f, 0f, 0f, 0f);
        GL11.glClearColor (0f, 0f, 0f, 0f);
        GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        // normal rendering to the FBO
        GL11.glEnable(GL11.GL_BLEND);
        //GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE_MINUS_SRC_ALPHA); // pre-multiplied alpha

        GL11.glViewport(0, 0, width, height);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        ShaderUniformHandle.DIRECTION.set(ShaderUniformHandle.VERTICAL);
        ShaderUniformHandle.DISTANCE.set(BLUR_DIST/height);
    }

    public void unbind() {
        // Switch back to normal framebuffer rendering
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
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

    public int getTexture() {
        return colorTexture;
    }

}
