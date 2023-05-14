CREATE TABLE file_meta_data
(
    id               UUID PRIMARY KEY,
    name             VARCHAR(512) NOT NULL,
    upload_date_time TIMESTAMP    NOT NULL,
    author_id        UUID         NOT NULL
);