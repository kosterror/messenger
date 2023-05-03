CREATE TABLE chat
(
    id               UUID
        CONSTRAINT chat_pk_id PRIMARY KEY,
    type             VARCHAR(64)
        CONSTRAINT chat_type_not_null NOT NULL,
    chat_name        VARCHAR(255),
    admin_id         UUID,
    date_of_creation DATE,
    avatar_id        UUID
);