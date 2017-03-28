package com.sang.rocketdog.transport;

public class BadRequestException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6487359571327137767L;
    /**
     * Creates a new instance.
     */
    public BadRequestException() {
    }

    /**
     * Creates a new instance.
     */
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Creates a new instance.
     */
    public BadRequestException(String message) {
        super(message);
    }

    /**
     * Creates a new instance.
     */
    public BadRequestException(Throwable cause) {
        super(cause);
    }
}
