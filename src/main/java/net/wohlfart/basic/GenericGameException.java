package net.wohlfart.basic;

/**
 * Exception used for setup problems that might happen when booting up or configuring the engine.
 */
@SuppressWarnings("serial")
public class GenericGameException extends RuntimeException {  // REVIEWED

    /**
     * Constructor for GenericGameException.
     *
     * @param message
     *            a {@link java.lang.String} giving some information about the exception and its cause.
     * @param cause
     *            a {@link java.lang.Throwable} object, usually the wrapped 'real' reason for this exception
     */
    public GenericGameException(String message, Throwable cause) {
        super(message, cause);
    }

    public GenericGameException(String message) {
        super(message);
    }

}
