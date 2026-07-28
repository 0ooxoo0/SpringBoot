package org.skypro.skyshop.exception;

public class NoSuchProductException extends RuntimeException {   // ← добавьте extends
    public NoSuchProductException(String message) {
        super(message);
    }
}