/*
 * Copyright (C) [2020] [Evergreen]
 * This program comes with ABSOLUTELY NO WARRANTY
 * This is free software, and you are welcome to redistribute it
 * under certain conditions
 */

package net.evergreen.client.exception;

public class IllegalAnnotationException extends RuntimeException {

    public IllegalAnnotationException(String reason) {
        super(reason);
    }

    public IllegalAnnotationException() {
        super();
    }

}
