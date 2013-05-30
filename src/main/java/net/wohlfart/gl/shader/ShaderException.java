package net.wohlfart.gl.shader;

/**
 * <p>
 * ShaderException class.
 * </p>
 * 
 * 
 * 
 */
@SuppressWarnings("serial")
public class ShaderException extends RuntimeException {

    ShaderException(String string) {
        super(string);
    }

    ShaderException(String string, Exception ex) {
        super(string, ex);
    }

}
