package net.wohlfart.basic;

/**
 * <p>Exception used for setup problems that might happen when booting up or configuring the engine.</p>
 */
@SuppressWarnings("serial")
public class GenericGameException extends RuntimeException {

    /**
     * <p>Constructor for GenericGameException.</p>
     *
     * @param message a {@link java.lang.String} object.
     * @param cause a {@link java.lang.Throwable} object.
     */
    public GenericGameException(String message, Throwable cause) {
        super (message, cause);
    }

}
