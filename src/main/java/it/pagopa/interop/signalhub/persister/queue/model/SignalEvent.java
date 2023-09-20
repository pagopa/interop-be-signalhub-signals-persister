package it.pagopa.interop.signalhub.persister.queue.model;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SignalEvent {
    private SignalType signalType;

    private String objectId;

    private String objectType;

    private String eserviceId;

    private Long indexSignal;
}
