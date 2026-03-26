create table QRCODE_RECIPIENT_COMPANY_LIST (
    ID uuid,
    VERSION integer,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255),
    OWNER_ID uuid not null,
    ACCESS_TYPE varchar(50),
    --
    primary key (ID)
);