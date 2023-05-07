CREATE TABLE notification
(
    id            UUID PRIMARY KEY,
    person_id     UUID         NOT NULL,
    type          VARCHAR(255) NOT NULL,
    message       VARCHAR(1028),
    received_date TIMESTAMP    NOT NULL,
    is_checked    BOOLEAN      NOT NULL,
    checked_date  TIMESTAMP
);