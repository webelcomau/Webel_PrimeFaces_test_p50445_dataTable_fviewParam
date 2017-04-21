package com.webel.test.primefaces;

/**
 *
 * @author darrenkelly
 */
public class InvalidUrlException extends Exception {

    /**
     * Creates a new instance of <code>InvalidUrlException</code> without detail message.
     */
    public InvalidUrlException() {
    }

    /**
     * Constructs an instance of <code>InvalidUrlException</code> with the specified detail message.
     *
     * @param msg the detail message.
     */
    public InvalidUrlException(String msg) {
        super(msg);
    }
}
