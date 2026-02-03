/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.service;

import com.company.qrcode.entity.EventRequest;
import com.haulmont.cuba.security.entity.User;
import com.google.zxing.*;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service(EventQrCodeService.NAME)
public class EventQrCodeServiceBean implements EventQrCodeService {

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
    public byte[] generateForParticipant(EventRequest req, User user) {
        try {
            String text = String.join("\n",
                    "Код мероприятия: " + req.getEventCode(),
                    "Мероприятие: " + req.getEventName(),
                    "Зал: " + (req.getEventHall() != null ? req.getEventHall().getInstanceName() : ""),
                    "Дата: " + (req.getEventDate() != null ? req.getEventDate().toString() : ""),
                    "Начало: " + (req.getTimeStart() != null ? req.getTimeStart().toString() : ""),
                    "Окончание: " + (req.getTimeEnd() != null ? req.getTimeEnd().toString() : ""),
                    "Фамилия: " + (user.getLastName() != null ? user.getLastName() : ""),
                    "Имя: " + (user.getFirstName() != null ? user.getFirstName() : ""),
                    "Должность: " + (user.getPosition() != null ? user.getPosition() : "")
            );

            System.out.println("Generating QR for text: " + text);
            byte[] result = generateQrCode(text);
            System.out.println("Generated " + (result != null ? result.length : 0) + " bytes");

            return result;

        } catch (Exception e) {
            System.err.println("Error generating QR: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}