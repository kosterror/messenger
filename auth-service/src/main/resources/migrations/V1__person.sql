CREATE TABLE person
(
    id            UUID PRIMARY KEY,
    date_of_birth DATE                NOT NULL,
    email         VARCHAR(255) UNIQUE NOT NULL,
    login         VARCHAR(255) UNIQUE NOT NULL,
    name          VARCHAR(255)        NOT NULL,
    password      VARCHAR(255)        NOT NULL,
    surname       VARCHAR(255) NULL,
    patronymic    VARCHAR(255)        NOT NULL
)