/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.service;

import com.company.qrcode.entity.EventRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;

@Service(EventCodeService.NAME)
public class EventCodeServiceBean implements EventCodeService {

    @Override
    public String generateEventCode(EventRequest req) {
        // Проверка на null
        if (req == null || req.getNumber() == null || req.getRequestDate() == null) {
            return "";
        }

        // Получаем данные
        int number = req.getNumber();
        int month = req.getRequestDate().getMonthValue();
        int year = req.getRequestDate().getYear() % 100; // Последние 2 цифры года
        String dateStr = req.getRequestDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

        // Форматируем код: number-month/year[date]
        return String.format("%d-%d/%02d[%s]", number, month, year, dateStr);
    }
}