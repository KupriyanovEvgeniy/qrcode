alter table QRCODE_QR_CODE rename column event_id to event_id__u34034 ;
alter table QRCODE_QR_CODE drop constraint FK_QRCODE_QR_CODE_ON_EVENT ;
drop index IDX_QRCODE_QR_CODE_ON_EVENT ;
alter table QRCODE_QR_CODE add column EVENT uuid ;
