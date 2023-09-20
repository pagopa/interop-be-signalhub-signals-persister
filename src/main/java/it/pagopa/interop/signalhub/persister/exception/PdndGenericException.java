package it.pagopa.interop.signalhub.persister.exception;

import lombok.Getter;


@Getter
public class PdndGenericException extends RuntimeException {
    private final ExceptionTypeEnum exceptionType;
    private final String message;


    public PdndGenericException(ExceptionTypeEnum exceptionType, String message){
        super(message);
        this.exceptionType = exceptionType;
        this.message = message;
    }
}