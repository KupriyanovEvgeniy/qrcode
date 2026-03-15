create table QRCODE_TEXT_TEMPLATE (
    ID uuid,
    VERSION integer,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    CODE varchar(20),
    CONTENT text,
    CATEGORY varchar(20),
    ACTIVE boolean,
    --
    primary key (ID)
);