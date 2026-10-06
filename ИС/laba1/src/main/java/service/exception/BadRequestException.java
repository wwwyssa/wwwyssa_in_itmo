package service.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
