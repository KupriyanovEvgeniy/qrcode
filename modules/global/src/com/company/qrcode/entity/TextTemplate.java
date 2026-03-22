/*
 * Copyright (c) 2026 LTD Haulmont Samara. All Rights Reserved.
 * Haulmont Samara proprietary and confidential.
 * Use is subject to license terms.
 */

package com.company.qrcode.entity;

import com.haulmont.cuba.core.entity.StandardEntity;
import com.haulmont.cuba.security.entity.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Table(name = "QRCODE_TEXT_TEMPLATE")
@Entity(name = "qrcode$TextTemplate")
public class TextTemplate extends StandardEntity {
    private static final long serialVersionUID = -400304369504998336L;

    @Column(name = "CODE", unique = true, length = 20)
    protected String code;

    @Lob
    @Column(name = "CONTENT")
    protected String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    protected TemplateCategories category;

    @Column(name = "ACTIVE")
    protected Boolean active;

    @NotNull
    @Column(name = "ACCESS_TYPE", nullable = false)
    protected String accessType;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OWNER_ID")
    protected User owner;

    @JoinTable(name = "QRCODE_TEXT_TEMPLATE_USER_LINK",
            joinColumns = @JoinColumn(name = "TEXT_TEMPLATE_ID"),
            inverseJoinColumns = @JoinColumn(name = "USER_ID"))
    @ManyToMany
    protected List<User> share;

    public TemplateCategories getCategory() {
        return category;
    }

    public void setCategory(TemplateCategories category) {
        this.category = category;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public AccessType getAccessType() {
        return accessType == null ? null : AccessType.fromId(accessType);
    }

    public void setAccessType(AccessType accessType) {
        this.accessType = accessType == null ? null : accessType.getId();
    }

    public List<User> getShare() {
        return share;
    }

    public void setShare(List<User> share) {
        this.share = share;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
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