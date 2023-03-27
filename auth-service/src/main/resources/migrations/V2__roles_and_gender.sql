ALTER TABLE person
    ADD COLUMN gender VARCHAR(255) NOT NULL DEFAULT 'MALE';

CREATE TABLE role
(
    id             UUID PRIMARY KEY,
    name           VARCHAR(255) NOT NULL
);

CREATE TABLE person_role
(
    person_id UUID REFERENCES person (id),
    role_id   UUID REFERENCES role (id)
);

