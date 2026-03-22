/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Table(name = "QRCODE_TEMPLATE_CATEGORIES")
@Entity(name = "qrcode$TemplateCategories")
@NamePattern("%s|name")
public class TemplateCategories extends StandardEntity {
    private static final long serialVersionUID = 2475458751829051992L;

    @Column(name = "NAME", length = 30)
    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}