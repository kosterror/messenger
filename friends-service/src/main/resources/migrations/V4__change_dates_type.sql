ALTER TABLE friend
    ALTER COLUMN date_of_addition TYPE DATE USING date_of_addition::date,
    ALTER COLUMN date_of_deletion TYPE DATE USING date_of_deletion::date;

ALTER TABLE blocked_person
    ALTER COLUMN date_of_addition TYPE DATE USING date_of_addition::date,
    ALTER COLUMN date_of_deletion TYPE DATE USING date_of_deletion::date;
