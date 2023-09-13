package it.pagopa.interop.signalhub.signals.persister;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.r2dbc.config.EnableR2dbcAuditing;

@Slf4j
@SpringBootApplication
@EnableR2dbcAuditing
public class SignalHubSignalsPersisterApplication {

	public static void main(String[] args) {

		log.error("AVAILABLE PROCESSOR: {}", Runtime.getRuntime().availableProcessors() );

		SpringApplication.run(SignalHubSignalsPersisterApplication.class, args);
	}

}
