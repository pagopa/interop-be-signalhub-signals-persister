package it.pagopa.interop.signalhub.persister.exception;

import lombok.Getter;


@Getter
public enum ExceptionTypeEnum{
    MAPPER_ERROR("MAPPER_ERROR", "The requested object could not be mapped."),
    DUPLICATE_SIGNAL_ERROR("DUPLICATE_SIGNAL_ERROR", "The signal is already saved on database.");

    private final String title;
    private final String message;


    ExceptionTypeEnum(String title, String message) {
        this.title = title;
        this.message = message;
    }
}