package net.wohlfart.basic;

/**
 * <p>
 * Exception used for setup problems that might happen when
 * booting up or configuring the engine.</p>
 */
@SuppressWarnings("serial")
public class GenericGameException extends RuntimeException {

    /**
     * <p>Constructor for GenericGameException.</p>
     *
     * @param message a {@link java.lang.String} giving some information about the exception and its cause.
     * @param cause a {@link java.lang.Throwable} object, usually the wrpaaed real reason for this exception
     */
    public GenericGameException(String message, Throwable cause) {
        super (message, cause);
    }

}
