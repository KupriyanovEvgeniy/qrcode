create table QRCODE_REQUEST_USER_LINK (
    REQUEST_ID uuid,
    USER_ID uuid,
    primary key (REQUEST_ID, USER_ID)
);
