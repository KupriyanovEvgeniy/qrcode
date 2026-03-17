/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity.support;

public class AddresseeRow {
    private UniquePerson person;
    private String name;
    private String type;

    public AddresseeRow(UniquePerson person, String name, String type) {
        this.person = person;
        this.name = name;
        this.type = type;
    }

    public UniquePerson getPerson() {
        return person;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }
}
