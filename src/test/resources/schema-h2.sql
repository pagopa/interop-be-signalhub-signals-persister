CREATE TABLE IF NOT EXISTS ESERVICE (
                                        eservice_id     VARCHAR (255) NOT NULL,
                                        producer_id     VARCHAR (255) NOT NULL,
                                        descriptor_id   VARCHAR (255) NOT NULL,
                                        event_id        BIGINT,
                                        state           VARCHAR (255) NOT NULL,
                                        tmst_insert     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                        tmst_last_edit  TIMESTAMP,
                                        UNIQUE (eservice_id, producer_id, descriptor_id),
                                        PRIMARY KEY (eservice_id, producer_id, descriptor_id)
);


CREATE TABLE IF NOT EXISTS CONSUMER_ESERVICE (
                                                 agreement_id    VARCHAR (255) NOT NULL,
                                                 eservice_id     VARCHAR (255) NOT NULL,
                                                 consumer_id     VARCHAR (255) NOT NULL,
                                                 descriptor_id   VARCHAR (255) NOT NULL,
                                                 event_id        BIGINT,
                                                 state           VARCHAR (255) NOT NULL,
                                                 tmst_insert     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                                 tmst_last_edit  TIMESTAMP,
                                                 UNIQUE (eservice_id, consumer_id, descriptor_id),
                                                 PRIMARY KEY (eservice_id, consumer_id, descriptor_id)
);


CREATE TABLE IF NOT EXISTS SIGNAL (
                                      id             SERIAL PRIMARY KEY,
                                      correlation_id VARCHAR(255) NOT NULL,
                                      signal_id      BIGINT        NOT NULL,
                                      object_id      VARCHAR (255)  NOT NULL,
                                      eservice_id    VARCHAR (255)  NOT NULL,
                                      object_type    VARCHAR (255)  NOT NULL,
                                      signal_type    VARCHAR (255)  NOT NULL,
                                      tmst_insert    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      UNIQUE (signal_id, eservice_id)
);


CREATE TABLE IF NOT EXISTS DEAD_SIGNAL (
                                           id             SERIAL PRIMARY KEY,
                                           correlation_id VARCHAR(255)   NOT NULL,
                                           signal_id      BIGINT        NOT NULL,
                                           object_id      VARCHAR (255)  NOT NULL,
                                           eservice_id    VARCHAR (255)  NOT NULL,
                                           object_type    VARCHAR (255)  NOT NULL,
                                           signal_type    VARCHAR (255)  NOT NULL,
                                           tmst_insert    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           error_reason   VARCHAR(255)  NOT NULL
);


CREATE TABLE IF NOT EXISTS TRACING_BATCH (
                                             batch_id         SERIAL PRIMARY KEY,
                                             state            VARCHAR (255) NOT NULL,
                                             type             VARCHAR (50) NOT NULL,
                                             last_event_id    BIGINT,
                                             tmst_created     TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS DEAD_EVENT (
                                          event_tmp_id        SERIAL PRIMARY KEY,
                                          tmst_insert         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                          error_reason        VARCHAR(255) NOT NULL,
                                          event_id            BIGINT NOT NULL,
                                          event_type          VARCHAR (255) NOT NULL,
                                          object_type         VARCHAR (255) NOT NULL,
                                          descriptor_id       VARCHAR (255),
                                          eservice_id         VARCHAR (255),
                                          agreement_id        VARCHAR (255)
);