CREATE TABLE person
(
    id            uuid
        CONSTRAINT not_null NOT NULL
        CONSTRAINT pk_id PRIMARY KEY,
    created_at    TIMESTAMP,
    updated_at    TIMESTAMP,
    login         VARCHAR(255)
        CONSTRAINT unique_login UNIQUE,
    email         VARCHAR(255)
        CONSTRAINT unique_email UNIQUE,
    password      VARCHAR(255),
    full_name     VARCHAR(255),
    date_of_birth DATE,
    phone_number  VARCHAR(255),
    city          VARCHAR(255),
    avatar_id     UUID
);
