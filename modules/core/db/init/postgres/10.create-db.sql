-- begin QRCODE_HALL
create table QRCODE_HALL (
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
    CAPACITY integer,
    DESCRIPTION varchar(255),
    --
    primary key (ID)
)^
-- end QRCODE_HALL
-- begin QRCODE_EVENT_REQUEST
create table QRCODE_EVENT_REQUEST (
    ID uuid,
    VERSION integer,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    EVENT_CODE varchar(255),
    EVENT_NAME varchar(255),
    EVENT_HALL_ID uuid,
    EVENT_DATE date,
    TIME_START time,
    TIME_END time,
    FIRST_NAME varchar(255),
    LAST_NAME varchar(255),
    MIDDLE_NAME varchar(255),
    ORGANIZATION varchar(255),
    QR_CODE bytea,
    REQUEST_DATE date,
    NUMBER_ integer,
    --
    primary key (ID)
)^
-- end QRCODE_EVENT_REQUEST
-- begin QRCODE_EVENT_PARTICIPANT
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
)^
-- end QRCODE_EVENT_PARTICIPANT
-- begin QRCODE_EXTERNAL_GUEST
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
)^
-- end QRCODE_EXTERNAL_GUEST
-- begin QRCODE_EVENT_EXTERNAL_PARTICIPANT
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
)^
-- end QRCODE_EVENT_EXTERNAL_PARTICIPANT
