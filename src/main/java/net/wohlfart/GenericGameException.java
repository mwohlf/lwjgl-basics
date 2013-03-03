package net.wohlfart;

@SuppressWarnings("serial")
public class GenericGameException extends RuntimeException {

    public GenericGameException(String message, Throwable cause) {
        super (message, cause);
    }

}
