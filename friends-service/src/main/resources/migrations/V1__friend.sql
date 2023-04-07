CREATE TABLE friend
(
    id               UUID         NOT NULL PRIMARY KEY,
    created_at       TIMESTAMP,
    updated_at       TIMESTAMP,
    owner_id         UUID         NOT NULL,
    member_id        UUID         NOT NULL,
    member_full_name VARCHAR(255) NOT NULL,
    date_of_deletion TIMESTAMP,
    is_deleted       BOOLEAN      NOT NULL
);