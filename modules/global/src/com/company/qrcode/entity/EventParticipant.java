/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;

@Table(name = "QRCODE_EVENT_PARTICIPANT")
@Entity(name = "qrcode$EventParticipant")
public class EventParticipant extends StandardEntity {
    private static final long serialVersionUID = -1944202429129301733L;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EVENT_REQUEST_ID")
    protected EventRequest eventRequest;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    protected User user;
    @Column(name = "QR_CODE")
    protected byte[] qrCode;

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public EventRequest getEventRequest() {
        return eventRequest;
    }

    public void setEventRequest(EventRequest eventRequest) {
        this.eventRequest = eventRequest;
    }
}