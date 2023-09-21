package it.pagopa.interop.signalhub.persister.repository;

import it.pagopa.interop.signalhub.persister.entity.Signal;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface SignalRepository extends ReactiveCrudRepository<Signal, Long> {

    @Query("SELECT * FROM SIGNAL WHERE signal_id = :signalId AND eservice_id = :eserviceId")
    Mono<Signal> findByIndexSignalAndEserviceId(Long signalId, String eserviceId);
}
