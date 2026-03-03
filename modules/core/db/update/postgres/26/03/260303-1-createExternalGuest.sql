create table QRCODE_EXTERNAL_GUEST (
    ID uuid,
    VERSION integer,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    LAST_NAME varchar(255),
    FIRST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    EMAIL varchar(255),
    PHONE varchar(255),
    ORGANIZATION varchar(255),
    POSITION_ varchar(255),
    PASSPORT_DATA varchar(255),
    COMMENT_ varchar(255),
    --
    primary key (ID)
);