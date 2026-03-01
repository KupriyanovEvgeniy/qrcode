/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.chile.core.datatypes.impl.EnumClass;

import javax.annotation.Nullable;


public enum RecipientListAccessType implements EnumClass<String> {

    GLOBAL("A"),
    PRIVATE("B");

    private String id;

    RecipientListAccessType(String value) {
        this.id = value;
    }

    public String getId() {
        return id;
    }

    @Nullable
    public static RecipientListAccessType fromId(String id) {
        for (RecipientListAccessType at : RecipientListAccessType.values()) {
            if (at.getId().equals(id)) {
                return at;
            }
        }
        return null;
    }
}