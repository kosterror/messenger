CREATE TABLE chat
(
    id               UUID PRIMARY KEY,
    type             varchar(64) NOT NULL,
    name             VARCHAR(128),
    admin_id         UUID,
    date_of_creation TIMESTAMP,
    avatar_id        UUID
);

CREATE TABLE relation_person
(
    id        UUID PRIMARY KEY,
    person_id UUID NOT NULL
);

CREATE TABLE message
(
    id                 UUID PRIMARY KEY,
    relation_person_id UUID REFERENCES relation_person (id),
    chat_id            UUID REFERENCES chat (id),
    date_of_sending    TIMESTAMP NOT NULL,
    text               VARCHAR(500)
);

CREATE TABLE attachment
(
    id         UUID PRIMARY KEY,
    message_id UUID REFERENCES message (id),
    file_id    UUID         NOT NULL,
    name       VARCHAR(128) NOT NULL
);

CREATE TABLE chat_person
(
    relation_person_id UUID REFERENCES relation_person (id),
    chat_id            UUID REFERENCES chat (id)
);