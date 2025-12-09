package ru.sspo.oos.exception;

/**
 * Исключение для бизнес-логики (невалидные операции).
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

