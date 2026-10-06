package service.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
