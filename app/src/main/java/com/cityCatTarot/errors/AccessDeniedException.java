package com.cityCatTarot.errors;

/**
 * 잘못된 접근 예외.
 */
public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(Long id) {
        super("Access denied: " + id);
    }
}
