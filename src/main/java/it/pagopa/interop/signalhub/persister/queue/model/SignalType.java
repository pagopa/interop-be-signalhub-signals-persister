package it.pagopa.interop.signalhub.persister.queue.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum SignalType {

    CREATE("CREATE"),

    UPDATE("UPDATE"),

    DELETE("DELETE"),

    SEEDUPDATE("SEEDUPDATE");

    private String code;

    SignalType(String code) {
        this.code = code;
    }

    @JsonValue()
    private String getCode() {
        return this.code;
    }

    @JsonCreator()
    private SignalType fromCode(String code) {
        for(SignalType signalType: SignalType.values()) {
            if(signalType.code.equals(code)) {
                return signalType;
            }
        }
        throw new IllegalArgumentException("Unexpected error on enum creation");
    }
}
