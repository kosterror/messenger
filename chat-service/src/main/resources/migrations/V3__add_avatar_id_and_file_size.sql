ALTER TABLE relation_person
    ADD COLUMN avatar_id UUID;

ALTER TABLE attachment
    ADD COLUMN size BIGINT;