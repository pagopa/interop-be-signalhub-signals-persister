
CREATE TABLE IF NOT EXISTS ORGANIZATION_ESERVICE (
    eservice_id     VARCHAR (50) UNIQUE NOT NULL,
    organization_id VARCHAR (50) NOT NULL,
    state           VARCHAR (50) NOT NULL,
    tmst_insert     TIMESTAMP    NOT NULL,
    tmst_last_edit  TIMESTAMP,
    UNIQUE (eservice_id, organization_id)
);


CREATE TABLE IF NOT EXISTS CONSUMER_ESERVICE (
    eservice_id     VARCHAR (50) NOT NULL,
    consumer_id     VARCHAR (50) NOT NULL,
    state           VARCHAR (50) NOT NULL,
    tmst_insert     TIMESTAMP    NOT NULL,
    tmst_last_edit  TIMESTAMP,
    UNIQUE (eservice_id, consumer_id)
);

CREATE TABLE IF NOT EXISTS SIGNAL (
    id SERIAL PRIMARY KEY,
    signal_id   BIGINT        NOT NULL,
    object_id   VARCHAR (50)  NOT NULL,
    eservice_id VARCHAR (50)  NOT NULL,
    object_type VARCHAR (50)  NOT NULL,
    signal_type VARCHAR (50)  NOT NULL,
    tmst_insert TIMESTAMP     NOT NULL,
    UNIQUE (signal_id, eservice_id)
);

/* H2 not support USING constructor
CREATE INDEX SIGNAL_INDEX_SIGNAL_ID ON SIGNAL USING hash (signal_id);
CREATE INDEX SIGNAL_INDEX_ESERVICE_ID ON SIGNAL USING hash (eservice_id);
*/

CREATE INDEX SIGNAL_INDEX_SIGNAL_ID ON SIGNAL (signal_id);
CREATE INDEX SIGNAL_INDEX_ESERVICE_ID ON SIGNAL (eservice_id);

CREATE TABLE IF NOT EXISTS EVENT_TEMP (
    event_temp_id    SERIAL PRIMARY KEY,
    event_id         BIGINT       UNIQUE NOT NULL,
    event_type       VARCHAR (50) NOT NULL,
    object_type      VARCHAR (50) NOT NULL,
    object_id        VARCHAR (50) NOT NULL,
    state            VARCHAR (50) NOT NULL,
    state_processing VARCHAR (50) NOT NULL,
    tmst_insert      TIMESTAMP    NOT NULL,
    tmst_processing  TIMESTAMP
);


CREATE TABLE IF NOT EXISTS DEAD_SIGNAL (
    id          SERIAL PRIMARY KEY,
    signal_id   BIGINT        NOT NULL,
    object_id   VARCHAR (50)  NOT NULL,
    eservice_id VARCHAR (50)  NOT NULL,
    object_type VARCHAR (50)  NOT NULL,
    signal_type VARCHAR (50)  NOT NULL,
    tmst_insert TIMESTAMP     NOT NULL,
    error VARCHAR(255)        NOT NULL
);