package service.exception;

import jakarta.ejb.ApplicationException;

@ApplicationException
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
