DROP TABLE IF EXISTS person_role, role;

ALTER TABLE person
    ADD COLUMN role varchar(255) NOT NULL DEFAULT 'USER';