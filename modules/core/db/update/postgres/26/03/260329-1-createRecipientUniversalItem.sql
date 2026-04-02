create table QRCODE_RECIPIENT_UNIVERSAL_ITEM (
    ID uuid,
    VERSION integer,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    LIST_ID uuid,
    ENTITY_ID uuid,
    ENTITY_NAME varchar(255),
    ENTITY_TYPE varchar(255),
    --
    primary key (ID)
);