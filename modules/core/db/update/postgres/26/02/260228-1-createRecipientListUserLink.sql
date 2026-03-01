create table QRCODE_RECIPIENT_LIST_USER_LINK (
    RECIPIENT_LIST_ID uuid,
    USER_ID uuid,
    primary key (RECIPIENT_LIST_ID, USER_ID)
);
