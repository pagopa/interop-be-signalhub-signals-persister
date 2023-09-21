package it.pagopa.interop.signalhub.persister.queue.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class SignalEvent {
    private SignalType signalType;

    private String objectId;

    private String objectType;

    private String eserviceId;

    private Long indexSignal;
}
