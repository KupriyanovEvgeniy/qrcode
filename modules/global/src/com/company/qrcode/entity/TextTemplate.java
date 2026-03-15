/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.cuba.core.entity.StandardEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

@Table(name = "QRCODE_TEXT_TEMPLATE")
@Entity(name = "qrcode$TextTemplate")
public class TextTemplate extends StandardEntity {
    private static final long serialVersionUID = -400304369504998336L;

    @Column(name = "CODE", unique = true, length = 20)
    protected String code;

    @Lob
    @Column(name = "CONTENT")
    protected String content;

    @Column(name = "CATEGORY", length = 20)
    protected String category;

    @Column(name = "ACTIVE")
    protected Boolean active;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}