package org.skypro.skyshop.exception;

public class NoSuchProductException extends RuntimeException {
    public NoSuchProductException() {
        super("Товар не найден");
    }

    public NoSuchProductException(String message) {
        super(message);
    }
}