/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.*;

@Table(name = "QRCODE_EVENT_EXTERNAL_PARTICIPANT")
@Entity(name = "qrcode$EventExternalParticipant")
@NamePattern("%s|guest")
public class EventExternalParticipant extends StandardEntity {
    private static final long serialVersionUID = -7622724998805991808L;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_REQUEST_ID")
    protected EventRequest eventRequest;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GUEST_ID")
    protected ExternalGuest guest;

    @Column(name = "QR_CODE")
    protected byte[] qrCode;

    @Column(name = "EMAIL_SENT")
    protected Boolean emailSent = false;

    @Column(name = "CHECKED_IN")
    protected Boolean checkedIn = false;

    public EventRequest getEventRequest() {
        return eventRequest;
    }

    public void setEventRequest(EventRequest eventRequest) {
        this.eventRequest = eventRequest;
    }

    public ExternalGuest getGuest() {
        return guest;
    }

    public void setGuest(ExternalGuest guest) {
        this.guest = guest;
    }

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    public Boolean getEmailSent() {
        return emailSent;
    }

    public void setEmailSent(Boolean emailSent) {
        this.emailSent = emailSent;
    }

    public Boolean getCheckedIn() {
        return checkedIn;
    }

    public void setCheckedIn(Boolean checkedIn) {
        this.checkedIn = checkedIn;
    }
}