--liquibase formatted sql

--changeset haralds:4

CREATE TABLE airport
(
    id      SERIAL PRIMARY KEY,
    airport VARCHAR(255) NOT NULL,
    city    VARCHAR(255) NOT NULL,
    country VARCHAR(255) NOT NULL
);
