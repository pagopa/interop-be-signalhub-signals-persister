package it.pagopa.interop.signalhub.persister.exception;

import lombok.Getter;


@Getter
public class PDNDGenericException extends RuntimeException {
    private final ExceptionTypeEnum exceptionType;
    private final String message;


    public PDNDGenericException(ExceptionTypeEnum exceptionType, String message){
        super(message);
        this.exceptionType = exceptionType;
        this.message = message;
    }
}