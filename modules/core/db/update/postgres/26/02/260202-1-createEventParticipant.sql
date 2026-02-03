create table QRCODE_EVENT_PARTICIPANT (
    ID uuid,
    VERSION integer,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    EVENT_REQUEST_ID uuid,
    USER_ID uuid,
    QR_CODE bytea,
    --
    primary key (ID)
);