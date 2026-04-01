create table QRCODE_TEMPLATE_CATEGORIES (
    ID uuid,
    VERSION integer,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(30),
    ACTIVE boolean,
    ACCESS_TYPE varchar(50),
    OWNER_ID uuid not null,
    --
    primary key (ID)
);