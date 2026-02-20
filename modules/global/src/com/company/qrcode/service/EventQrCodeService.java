/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.service;

import com.company.qrcode.entity.EventRequest;
import com.haulmont.cuba.security.entity.User;
import com.company.qrcode.entity.EventParticipant;
import com.company.qrcode.entity.EventExternalParticipant;

import java.util.UUID;

public interface EventQrCodeService {
    String NAME = "qrcode_EventQrCodeService";

    byte[] generateQrCode(String qrText);

    byte[] generate(UUID eventId, UUID participantId, String participantType);
}