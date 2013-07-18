package net.wohlfart.basic.container;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import net.wohlfart.basic.elements.IsUpdatable;
import net.wohlfart.gl.elements.TexturedQuad;
import net.wohlfart.gl.shader.mesh.AbstractMeshBuilder;
import net.wohlfart.tools.PNGDecoder;
import net.wohlfart.tools.PNGDecoder.Format;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector3f;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// @formatter:off
/**
 * this class does an extra render pass to create a glow overlay
 *
 * see: http://www.gamasutra.com/view/feature/130520/realtime_glow.php?page=2
 *      http://www.lwjgl.org/wiki/index.php?title=Using_Frame_Buffer_Objects_%28FBO%29
 *      https://github.com/mattdesl/lwjgl-basics/wiki/ShaderLesson5
 *      http://lwjgl.org/forum/index.php?topic=4269.0;wap2
 */
public class GlowRenderSet extends DefaultRenderBatch<IsUpdatable> { // @formatter:on
    static final Logger LOGGER = LoggerFactory.getLogger(AbstractMeshBuilder.class);


    int width = 1024;
    int height = 1024;

    int maxsamples = 3;


    private int framebufferID;


    private int colorTextureID;


    private int depthRenderBufferID;


    @Override
    public void setup() {
        super.setup();
        fboSetup();
    }



    @Override
    public void render() {

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        // switch to rendering on our FBO
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID);
        // Clear Screen And Depth Buffer on the fbo to red
        //GL11.glClearColor (1.0f, 0.0f, 0.0f, 0.5f);
        GL11.glClear (GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        // normal rendering on the fbo
        super.render();

        // back to screen rendering
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);


        TexturedQuad quad1 = new TexturedQuad();
        quad1.setPosition(new Vector3f(0,0,-10));
        quad1.setTexHandle(colorTextureID);
        quad1.setTexFilename(null);
        quad1.render();

        TexturedQuad quad2 = new TexturedQuad();
        quad2.setPosition(new Vector3f(0,5,-10));
        quad2.setTexHandle(colorTextureID);
        quad2.setTexFilename(null);
        quad2.render();

        TexturedQuad quad3 = new TexturedQuad();
        quad3.setPosition(new Vector3f(0,-5,-10));
        quad3.setTexHandle(colorTextureID);
        quad3.setTexFilename(null);
        quad3.render();


    }



    public void fboSetup() {

        // create a new framebuffer
        framebufferID = EXTFramebufferObject.glGenFramebuffersEXT();
        // and a new texture used as a color buffer
        colorTextureID = GL11.glGenTextures();
        // And finally a new depthbuffer
        depthRenderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();

        // switch to the new framebuffer
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID);

        // initialize color texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);                                   // Bind the colorbuffer texture
        GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);               // make it linear filterd
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);  // Create the texture data
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTextureID, 0); // attach it to the framebuffer

        // initialize depth renderbuffer
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID);                // bind the depth renderbuffer
        EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, width, height); // get the data space for it
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID); // bind it to the renderbuffer

        checkFBO(framebufferID);


        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);                                    // Switch back to normal framebuffer rendering

    }

    /*
        // we create a new framebuffer
        int fboHandle = EXTFramebufferObject.glGenFramebuffersEXT();
        // switch to the new framebuffer
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandle);

        // we allocate a depth buffer
        int depthHandle = EXTFramebufferObject.glGenRenderbuffersEXT();
        // bind the depth renderbuffer
        EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthHandle);
        // get the data space for it
        EXTFramebufferMultisample.glRenderbufferStorageMultisampleEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, maxsamples,EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, width, height);

        // we bind the same buffer twice: once as depth buffer, once as stencil
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT,EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthHandle);
        EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT,EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT,EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthHandle);



        // create the texture
        int texHandle = GL11.glGenTextures();
        ByteBuffer texturebuffer = BufferUtils.createByteBuffer(screenwidth*screenheight*4);
        texturebuffer.order(ByteOrder.nativeOrder());

        for(int i=0; i<screenwidth*screenheight/4; i++) {
            texturebuffer.put((byte) 255);
            texturebuffer.put((byte) 255);
            texturebuffer.put((byte) 255);
            texturebuffer.put((byte) 255);
        }
        texturebuffer.flip();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texHandle);
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, screenwidth/2, screenheight/2, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, texturebuffer);

        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, fboHandle);
        EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT,
                GL11.GL_TEXTURE_2D, texHandle, 0);

        int framebuffer = EXTFramebufferObject.glCheckFramebufferStatusEXT( EXTFramebufferObject.GL_FRAMEBUFFER_EXT );
        if (framebuffer != EXTFramebufferObject.GL_FRAMEBUFFER_COMPLETE_EXT) {
            throw new IllegalStateException("error: " + framebuffer);
        }

        // test texture:
        //final int texHandle = loadPNGTexture("/models/cube/cube.png", GL13.GL_TEXTURE0);  // also binds the texture
        super.render();

        // back to screen rendering
        EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);


        TexturedQuad quad = new TexturedQuad();
        quad.setPosition(new Vector3f(0,0,-10));
        quad.setTexHandle(texHandle);
        quad.setTexFilename(null);
        quad.render();

    }
     */

    private int loadPNGTexture(String filename, int textureUnit) {
        int texHandle = -1;

        // InputStream inputStream = new FileInputStream(filename);
        try (InputStream inputStream = ClassLoader.class.getResourceAsStream(filename)) {

            // Link the PNG decoder to this stream
            final PNGDecoder decoder = new PNGDecoder(inputStream);
            // Get the width and height of the texture
            final int tWidth = decoder.getWidth();
            final int tHeight = decoder.getHeight();
            // Decode the PNG file in a ByteBuffer
            final ByteBuffer buffer = ByteBuffer.allocateDirect(4 * decoder.getWidth() * decoder.getHeight());
            decoder.decode(buffer, decoder.getWidth() * 4, Format.RGBA);
            buffer.flip();

            // Create a new texture object in memory and bind it
            texHandle = GL11.glGenTextures();
            GL13.glActiveTexture(textureUnit);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texHandle);
            // All RGB bytes are aligned to each other and each component is 1 byte
            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
            // Upload the texture data and generate mip maps (for scaling)
            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, tWidth, tHeight, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);
            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
            // Setup the ST coordinate system
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            // Setup what to do when the texture has to be scaled
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

        } catch (final FileNotFoundException ex) {
            LOGGER.error("can't load texture image", ex);
        } catch (final IOException ex) {
            LOGGER.error("can't load texture image", ex);
        }
        return texHandle;
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
