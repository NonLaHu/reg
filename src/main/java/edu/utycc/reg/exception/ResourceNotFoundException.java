package edu.utycc.reg.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String identifierName, Object identifierValue) {
        super(String.format("%s not found with %s : '%s'", resource, identifierName, identifierValue));
    }
}
