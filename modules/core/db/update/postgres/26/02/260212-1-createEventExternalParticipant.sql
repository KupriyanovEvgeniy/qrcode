create table QRCODE_EVENT_EXTERNAL_PARTICIPANT (
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
    GUEST_ID uuid,
    QR_CODE bytea,
    EMAIL_SENT boolean,
    CHECKED_IN boolean,
    --
    primary key (ID)
);