package net.wohlfart.gl.shader;

import net.wohlfart.basic.GenericGameException;


@SuppressWarnings("serial")
public class ShaderException extends GenericGameException {  // REVIEWED

    ShaderException(String string) {
        super(string);
    }

    ShaderException(String string, Exception ex) {
        super(string, ex);
    }

}
