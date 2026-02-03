/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.service;

import com.company.qrcode.entity.EventRequest;

public interface EventCodeService {
    String NAME = "qrcode_EventCodeService";

    String generateEventCode(EventRequest req);
}