package it.pagopa.interop.signalhub.persister.repository;

import it.pagopa.interop.signalhub.persister.entity.Signal;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SignalRepository extends ReactiveCrudRepository<Signal, Long> {
}
