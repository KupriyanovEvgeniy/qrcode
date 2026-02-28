create table QRCODE_QR_CODE (
    ID uuid,
    VERSION integer,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    EVENT uuid,
    PARTICIPANT_TYPE varchar(255),
    PARTICIPANT_ID uuid,
    --
    primary key (ID)
);