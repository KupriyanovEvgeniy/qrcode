/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;


import java.util.UUID;

public interface EventParticipantView {

    UUID getParticipantId();

    String getFullName();

    byte[] getQrCode();

    void setQrCode(byte[] qrCode);

    String getParticipantType();

}