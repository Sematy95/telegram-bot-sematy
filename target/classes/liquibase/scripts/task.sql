-- liquibase formatted sql

--changeset Sematy95:1
CREATE TABLE if not exists "notification_task"
(
    ID                BIGSERIAL PRIMARY KEY,
    CHAT_ID           VARCHAR   NOT NULL,
    MESSAGE           VARCHAR   NOT NULL,
    NOTIFICATION_DATE TIMESTAMP NOT NULL,
    CREATION_DATE     TIMESTAMP NOT NULL
);

