/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Table(name = "QRCODE_QR_CODE")
@Entity(name = "qrcode$QrCode")
public class QrCode extends StandardEntity {
    private static final long serialVersionUID = 3935661245130918784L;

    @Column(name = "EVENT")
    protected UUID eventId;

    @Column(name = "PARTICIPANT_TYPE")
    protected String participantType;

    @Column(name = "PARTICIPANT_ID")
    protected UUID participantId;

    public void setEventId(UUID eventId) {
        this.eventId = eventId;
    }

    public UUID getEventId() {
        return eventId;
    }

    public void setParticipantId(UUID participantId) { this.participantId = participantId; }
    public String getParticipantType() { return participantType; }
    public void setParticipantType(String participantType) { this.participantType = participantType; }
    public UUID getParticipantId() { return participantId; }
}