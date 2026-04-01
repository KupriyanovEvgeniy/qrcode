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
-- begin QRCODE_QR_CODE
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
)^
-- end QRCODE_QR_CODE
-- begin QRCODE_TEMPLATE_CATEGORIES
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
)^
-- end QRCODE_TEMPLATE_CATEGORIES
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
-- begin QRCODE_RECIPIENT_LIST
create table QRCODE_RECIPIENT_LIST (
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
    ACCESS_TYPE varchar(50),
    OWNER_ID uuid not null,
    --
    primary key (ID)
)^
-- end QRCODE_RECIPIENT_LIST
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
-- begin QRCODE_REQUEST
create table QRCODE_REQUEST (
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
    --
    primary key (ID)
)^
-- end QRCODE_REQUEST
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
-- begin QRCODE_TEXT_TEMPLATE
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
    OWNER_ID uuid not null,
    CATEGORY_ID uuid,
    --
    primary key (ID)
)^
-- end QRCODE_TEXT_TEMPLATE
-- begin QRCODE_REQUEST_USER_LINK
create table QRCODE_REQUEST_USER_LINK (
    REQUEST_ID uuid,
    USER_ID uuid,
    primary key (REQUEST_ID, USER_ID)
)^
-- end QRCODE_REQUEST_USER_LINK
-- begin QRCODE_TEMPLATE_CATEGORIES_USER_LINK
create table QRCODE_TEMPLATE_CATEGORIES_USER_LINK (
    TEMPLATE_CATEGORIES_ID uuid,
    USER_ID uuid,
    primary key (TEMPLATE_CATEGORIES_ID, USER_ID)
)^
-- end QRCODE_TEMPLATE_CATEGORIES_USER_LINK
-- begin QRCODE_RECIPIENT_LIST_USER_LINK
create table QRCODE_RECIPIENT_LIST_USER_LINK (
    RECIPIENT_LIST_ID uuid,
    USER_ID uuid,
    primary key (RECIPIENT_LIST_ID, USER_ID)
)^
-- end QRCODE_RECIPIENT_LIST_USER_LINK
-- begin QRCODE_RECIPIENT_LIST_SHARE_LINK
create table QRCODE_RECIPIENT_LIST_SHARE_LINK (
    RECIPIENT_LIST_ID uuid,
    USER_ID uuid,
    primary key (RECIPIENT_LIST_ID, USER_ID)
)^
-- end QRCODE_RECIPIENT_LIST_SHARE_LINK
