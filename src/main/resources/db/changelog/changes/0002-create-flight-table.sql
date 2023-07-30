--liquibase formatted sql

--changeset haralds:6

CREATE TABLE flight
(
    id             SERIAL PRIMARY KEY,
    from_airport   INT REFERENCES airport (id),
    to_airport     INT REFERENCES airport (id),
    carrier_name   VARCHAR(255) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time   TIMESTAMP NOT NULL
);