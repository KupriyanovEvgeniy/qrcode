/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.service;

import com.company.qrcode.entity.EventRequest;
import com.company.qrcode.entity.QrCode;
import com.haulmont.cuba.core.global.DataManager;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service(EventQrCodeService.NAME)
public class EventQrCodeServiceBean implements EventQrCodeService {

    @Inject
    private DataManager dataManager;

    @Override
    public byte[] generateQrCode(String qrText) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, StandardCharsets.UTF_8.name());

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(qrText, BarcodeFormat.QR_CODE, 300, 300, hints);

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);

            return outputStream.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка генерации QR-кода", e);
        }
    }

    @Override
    public byte[] generate(UUID eventId, UUID participantId, String participantType) {
        QrCode qr = dataManager.create(QrCode.class);
        qr.setEventId(eventId);
        qr.setParticipantType(participantType);
        qr.setParticipantId(participantId);
        qr = dataManager.commit(qr);
        return generateQrCode(qr.getId().toString());
    }
}