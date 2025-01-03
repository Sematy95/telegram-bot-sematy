--liquibase formatted sql

--changeSet Sematy95:1
CREATE TABLE task(
                     ID BIGSERIAL PRIMARY KEY ,
                     CHAT_ID VARCHAR NOT NULL ,
                     REQUEST VARCHAR NOT NULL,
                     MESSAGE VARCHAR NOT NULL,
                     NOTIFICATION_DATE TIMESTAMP NOT NULL,
                     CREATION_DATE TIMESTAMP NOT NULL
)